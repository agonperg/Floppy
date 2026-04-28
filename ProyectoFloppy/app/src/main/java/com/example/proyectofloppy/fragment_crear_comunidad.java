package com.example.proyectofloppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class fragment_crear_comunidad extends Fragment {

    private TextInputEditText etNombre;
    private MaterialButton btnCrearFinal;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_comunidad, container, false);

        // Inicializar instancias de Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etNombre = view.findViewById(R.id.etNombreComunidad);
        btnCrearFinal = view.findViewById(R.id.btnCrearFinal);

        btnCrearFinal.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            if (!nombre.isEmpty()) {
                guardarComunidad(nombre);
            } else {
                etNombre.setError("Escribe un nombre para la comunidad");
            }
        });
        return view;
    }

    private void guardarComunidad(String nombre) {
        // Obtener el ID del usuario actual (si no hay, usa un ID de prueba)
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "admin_test";

        // Crear mapa de datos para Firestore
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", nombre);
        data.put("ubicacion", "Ubicación por defecto");
        data.put("creadorId", userId); // Guardamos quién es el admin para el chat

        // Guardar en la colección global "Comunidades"
        db.collection("Comunidades").add(data).addOnSuccessListener(doc -> {
            String comunidadId = doc.getId();

            // Una vez creada, la añadimos automáticamente al perfil del usuario
            db.collection("users").document(userId)
                    .update("misComunidades", FieldValue.arrayUnion(comunidadId))
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Comunidad creada y vinculada", Toast.LENGTH_SHORT).show();
                        // Volver atrás a la pantalla principal
                        getParentFragmentManager().popBackStack();
                    });
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Error al guardar en Firebase", Toast.LENGTH_SHORT).show();
        });
    }
}