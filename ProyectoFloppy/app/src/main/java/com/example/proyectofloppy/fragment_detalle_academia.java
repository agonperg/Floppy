package com.example.proyectofloppy;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class fragment_detalle_academia extends Fragment {

    private String academiaId = "";
    private String nombre = "";
    private String direccion = "";
    private String descripcion = "";
    private String creadorId = "";

    private TextView tvNombre, tvDireccion, tvDescripcion;
    private MaterialButton btnEditar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public fragment_detalle_academia() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_academia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ImageView btnBack = view.findViewById(R.id.btn_back_detalle);
        tvNombre = view.findViewById(R.id.tv_nombre_academia_detalle);
        tvDireccion = view.findViewById(R.id.tv_direccion_academia_detalle);
        tvDescripcion = view.findViewById(R.id.tv_descripcion_academia_detalle);
        btnEditar = view.findViewById(R.id.btn_editar_academia);

        // Load arguments
        if (getArguments() != null) {
            academiaId = getArguments().getString("id", "");
            nombre = getArguments().getString("nombre", "");
            direccion = getArguments().getString("direccion", "");
            descripcion = getArguments().getString("descripcion", "");
            creadorId = getArguments().getString("creadorId", "");
        }

        // Set text views
        tvNombre.setText(nombre);
        tvDireccion.setText(direccion);
        tvDescripcion.setText(descripcion);

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        // Show edit button if current user is the creator
        String currentUid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";
        if (!currentUid.isEmpty() && currentUid.equals(creadorId)) {
            btnEditar.setVisibility(View.VISIBLE);
        } else {
            btnEditar.setVisibility(View.GONE);
        }

        btnEditar.setOnClickListener(v -> mostrarDialogoEdicion());
    }

    private void mostrarDialogoEdicion() {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Editar Información");

        // Create edit text fields dynamically inside a vertical layout
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        final EditText inputNombre = new EditText(getContext());
        inputNombre.setHint("Nombre de la Academia");
        inputNombre.setText(nombre);
        layout.addView(inputNombre);

        // Add padding to subsequent inputs
        View spacer1 = new View(getContext());
        spacer1.setMinimumHeight(20);
        layout.addView(spacer1);

        final EditText inputDireccion = new EditText(getContext());
        inputDireccion.setHint("Dirección completa");
        inputDireccion.setText(direccion);
        layout.addView(inputDireccion);

        View spacer2 = new View(getContext());
        spacer2.setMinimumHeight(20);
        layout.addView(spacer2);

        final EditText inputDescripcion = new EditText(getContext());
        inputDescripcion.setHint("Descripción de la academia...");
        inputDescripcion.setText(descripcion);
        inputDescripcion.setMinLines(3);
        layout.addView(inputDescripcion);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = inputNombre.getText().toString().trim();
            String nuevaDireccion = inputDireccion.getText().toString().trim();
            String nuevaDescripcion = inputDescripcion.getText().toString().trim();

            if (nuevoNombre.isEmpty() || nuevaDireccion.isEmpty() || nuevaDescripcion.isEmpty()) {
                Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            actualizarAcademiaEnFirestore(nuevoNombre, nuevaDireccion, nuevaDescripcion);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void actualizarAcademiaEnFirestore(String nuevoNombre, String nuevaDireccion, String nuevaDescripcion) {
        if (academiaId.isEmpty()) return;

        db.collection("academias").document(academiaId)
                .update(
                        "nombre", nuevoNombre,
                        "direccion", nuevaDireccion,
                        "descripcion", nuevaDescripcion
                )
                .addOnSuccessListener(aVoid -> {
                    nombre = nuevoNombre;
                    direccion = nuevaDireccion;
                    descripcion = nuevaDescripcion;

                    tvNombre.setText(nombre);
                    tvDireccion.setText(direccion);
                    tvDescripcion.setText(descripcion);

                    Toast.makeText(getContext(), "Información actualizada con éxito", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}