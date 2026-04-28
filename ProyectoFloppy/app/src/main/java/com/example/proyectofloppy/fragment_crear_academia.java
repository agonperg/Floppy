package com.example.proyectofloppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;

public class fragment_crear_academia extends Fragment {

    private EditText etNombre, etDireccion, etDescripcion;
    private MaterialButton btnGuardar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String academiaId = null;

    public fragment_crear_academia() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_academia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etNombre = view.findViewById(R.id.et_nombre_academia);
        etDireccion = view.findViewById(R.id.et_direccion_academia);
        etDescripcion = view.findViewById(R.id.et_descripcion_academia);
        btnGuardar = view.findViewById(R.id.btn_guardar_academia);
        ImageView btnBack = view.findViewById(R.id.btn_back_crear);

        if (getArguments() != null && getArguments().containsKey("academia_id")) {
            academiaId = getArguments().getString("academia_id");
            cargarDatosAcademia();
            btnGuardar.setText("Guardar Cambios");
        }

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        btnGuardar.setOnClickListener(v -> guardarAcademia());
    }

    private void cargarDatosAcademia() {
        db.collection("academias").document(academiaId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Academia academia = documentSnapshot.toObject(Academia.class);
                        if (academia != null) {
                            etNombre.setText(academia.getNombre());
                            etDireccion.setText(academia.getDireccion());
                            etDescripcion.setText(academia.getDescripcion());
                        }
                    }
                });
    }

    private void guardarAcademia() {
        String nombre = etNombre.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || direccion.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = (academiaId != null) ? academiaId : UUID.randomUUID().toString();
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "anonimo";

        Academia academia = new Academia(id, nombre, descripcion, direccion, userId);

        db.collection("academias").document(id).set(academia)
                .addOnSuccessListener(aVoid -> {
                    String msg = (academiaId != null) ? "Academia actualizada" : "Academia creada";
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
