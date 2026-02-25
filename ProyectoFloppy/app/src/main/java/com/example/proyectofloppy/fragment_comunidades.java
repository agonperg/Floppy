package com.example.proyectofloppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class fragment_comunidades extends Fragment {

    private RecyclerView recyclerView;
    private ComunidadAdapter adapter;
    private List<Comunidad> listaComunidades;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comunidades, container, false);

        // 1. Inicializar botones
        Button btnCrear = view.findViewById(R.id.btnCrearComunidad);
        Button btnUnirse = view.findViewById(R.id.btnUnirme);

        // 2. Configurar RecyclerView
        recyclerView = view.findViewById(R.id.rvComunidades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaComunidades = new ArrayList<>();
        adapter = new ComunidadAdapter(listaComunidades);
        recyclerView.setAdapter(adapter);

        // 3. Inicializar Firebase y cargar datos reales
        db = FirebaseFirestore.getInstance();
        cargarComunidadesDesdeFirebase();

        // 4. Click Listeners
        btnCrear.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Abriendo creador de comunidades...", Toast.LENGTH_SHORT).show();
            // Aquí irá la navegación a la pantalla de crear
        });

        return view;
    }

    private void cargarComunidadesDesdeFirebase() {
        // Asegúrate de que tu compañero llamó a la colección "Comunidades" (con C mayúscula)
        db.collection("Comunidades").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listaComunidades.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    Comunidad comunidad = document.toObject(Comunidad.class);
                    if (comunidad != null) {
                        comunidad.setId(document.getId());
                        listaComunidades.add(comunidad);
                    }
                }
                adapter.notifyDataSetChanged();

                if (listaComunidades.isEmpty()) {
                    Toast.makeText(getContext(), "No hay comunidades en la base de datos", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Error de conexión: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}