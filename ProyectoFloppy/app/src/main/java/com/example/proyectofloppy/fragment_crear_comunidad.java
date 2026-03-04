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
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class fragment_crear_comunidad extends Fragment {

    private TextInputEditText etNombre;
    private MaterialButton btnCrearFinal;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos tu diseño
        View view = inflater.inflate(R.layout.fragment_crear_comunidad, container, false);

        // Conectamos con Firebase
        db = FirebaseFirestore.getInstance();

        // Enlazamos las variables con los IDs de tu XML
        etNombre = view.findViewById(R.id.etNombreComunidad);
        btnCrearFinal = view.findViewById(R.id.btnCrearFinal);

        // Qué pasa cuando pulsas el botón "Crear Comunidad"
        btnCrearFinal.setOnClickListener(v -> {
            String nombreComunidad = etNombre.getText().toString().trim();

            if (nombreComunidad.isEmpty()) {
                etNombre.setError("Por favor, escribe un nombre");
                return;
            }

            // Si hay texto, lo guardamos en Firebase
            guardarEnFirebase(nombreComunidad);
        });

        return view;
    }

    private void guardarEnFirebase(String nombre) {
        // Preparamos los datos (le pongo una descripción fija porque en el diseño aún no hay campo para ella)
        Map<String, Object> nuevaComunidad = new HashMap<>();
        nuevaComunidad.put("nombre", nombre);
        nuevaComunidad.put("descripcion", "Comunidad creada desde la app");

        // Lo subimos a la colección "Comunidades"
        db.collection("Comunidades").add(nuevaComunidad)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "¡Comunidad creada!", Toast.LENGTH_SHORT).show();
                    // Volvemos automáticamente a la lista de comunidades
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al crear: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}