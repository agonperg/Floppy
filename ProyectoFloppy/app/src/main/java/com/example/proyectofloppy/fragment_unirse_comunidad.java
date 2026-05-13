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

import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unirse_comunidad, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Button btnVolver = view.findViewById(R.id.btnVolver);
        EditText etBuscador = view.findViewById(R.id.etBuscadorUnirse);

        btnVolver.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        recyclerView = view.findViewById(R.id.rvUnirseComunidades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaTodas = new ArrayList<>();
        listaOriginal = new ArrayList<>();
        
        adapter = new ComunidadAdapter(listaTodas);
        adapter.setEsModoBusqueda(true); // MODO BÚSQUEDA: Icono de añadir/tick
        
        recyclerView.setAdapter(adapter);

        adapter.setOnAccionClickListener(comunidad -> {
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
        if (comunidad == null || comunidad.getId() == null) return;

        com.google.firebase.auth.FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), getString(R.string.comunidad_sesion_no_iniciada), Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();

        db.collection("users").document(userId)
                .update("misComunidades", FieldValue.arrayUnion(comunidad.getId()))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), getString(R.string.comunidad_unido_exito, comunidad.getNombre()), Toast.LENGTH_SHORT).show();
                    // Opcional: volver atrás automáticamente
                    getParentFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), getString(R.string.comunidad_error_perfil), Toast.LENGTH_LONG).show();
                });
    }
}