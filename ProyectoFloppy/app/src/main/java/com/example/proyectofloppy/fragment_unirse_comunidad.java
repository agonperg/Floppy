package com.example.proyectofloppy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class fragment_unirse_comunidad extends Fragment {

    private RecyclerView recyclerView;
    private ComunidadAdapter adapter;
    private List<Comunidad> listaTodas;
    private List<Comunidad> listaOriginal;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unirse_comunidad, container, false);
        db = FirebaseFirestore.getInstance();

        Button btnVolver = view.findViewById(R.id.btnVolver);
        EditText etBuscador = view.findViewById(R.id.etBuscadorUnirse);

        btnVolver.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        recyclerView = view.findViewById(R.id.rvUnirseComunidades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaTodas = new ArrayList<>();
        listaOriginal = new ArrayList<>();
        adapter = new ComunidadAdapter(listaTodas);
        recyclerView.setAdapter(adapter);

        // Al clicar, ahora guardamos en el ARRAY del usuario
        adapter.setOnItemClickListener(comunidad -> {
            unirseAComunidadEnUsuario(comunidad);
        });

        if (etBuscador != null) {
            etBuscador.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void afterTextChanged(Editable s) { filtrar(s.toString()); }
            });
        }

        cargarTodasLasComunidades();
        return view;
    }

    private void cargarTodasLasComunidades() {
        db.collection("Comunidades").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listaTodas.clear();
                listaOriginal.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    Comunidad c = document.toObject(Comunidad.class);
                    if (c != null) {
                        c.setId(document.getId());
                        listaTodas.add(c);
                    }
                }
                listaOriginal.addAll(listaTodas);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void filtrar(String texto) {
        List<Comunidad> filtrada = new ArrayList<>();
        for (Comunidad c : listaOriginal) {
            if (c.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtrada.add(c);
            }
        }
        adapter.filtrar(filtrada);
    }

    private void unirseAComunidadEnUsuario(Comunidad comunidad) {
        com.google.firebase.auth.FirebaseAuth mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "admin_test";

        db.collection("users").document(userId)
                .update("misComunidades", FieldValue.arrayUnion(comunidad.getId()))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Añadida a tu perfil: " + comunidad.getNombre(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Si no existe, lo creamos
                    java.util.Map<String, Object> data = new java.util.HashMap<>();
                    data.put("misComunidades", java.util.Arrays.asList(comunidad.getId()));
                    db.collection("users").document(userId).set(data).addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Añadida a tu perfil: " + comunidad.getNombre(), Toast.LENGTH_SHORT).show();
                    });
                });
    }
}