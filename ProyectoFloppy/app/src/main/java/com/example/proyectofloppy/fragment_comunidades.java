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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class fragment_comunidades extends Fragment {

    private RecyclerView recyclerView;
    private ComunidadAdapter adapter;
    private List<Comunidad> listaComunidades;
    private List<Comunidad> listaOriginal;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comunidades, container, false);

        // Inicialización de Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referencias UI
        Button btnCrear = view.findViewById(R.id.btnCrearComunidad);
        Button btnUnirse = view.findViewById(R.id.btnUnirme);
        EditText etBuscar = view.findViewById(R.id.etBuscadorComunidad);

        // Configuración RecyclerView
        recyclerView = view.findViewById(R.id.rvComunidades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaComunidades = new ArrayList<>();
        listaOriginal = new ArrayList<>();

        adapter = new ComunidadAdapter(listaComunidades);
        recyclerView.setAdapter(adapter);

        // Buscador
        if (etBuscar != null) {
            etBuscar.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void afterTextChanged(Editable s) {
                    filtrarBuscador(s.toString());
                }
            });
        }

        // Navegación corregida (asegúrate que las clases existan con estos nombres)
        btnCrear.setOnClickListener(v -> navegarAFragment(new fragment_crear_comunidad()));
        btnUnirse.setOnClickListener(v -> navegarAFragment(new fragment_unirse_comunidad()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarComunidadesDelUsuario();
    }

    private void cargarComunidadesDelUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Sacamos la lista del array "misComunidades"
                    Object misComunidadesObj = documentSnapshot.get("misComunidades");

                    if (misComunidadesObj instanceof List) {
                        List<String> ids = (List<String>) misComunidadesObj;

                        if (!ids.isEmpty()) {
                            // Consultamos los datos de las comunidades cuyos IDs están en el array
                            db.collection("Comunidades")
                                    .whereIn(FieldPath.documentId(), ids)
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        listaComunidades.clear();
                                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                            Comunidad c = doc.toObject(Comunidad.class);
                                            if (c != null) {
                                                c.setId(doc.getId());
                                                listaComunidades.add(c);
                                            }
                                        }
                                        listaOriginal.clear();
                                        listaOriginal.addAll(listaComunidades);
                                        adapter.notifyDataSetChanged();
                                    });
                        } else {
                            limpiarLista();
                        }
                    } else {
                        limpiarLista();
                    }
                }
            }).addOnFailureListener(e -> {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Error al acceder al perfil", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            if (isAdded()) {
                Toast.makeText(getContext(), "Sesión no iniciada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void filtrarBuscador(String textoBusqueda) {
        List<Comunidad> filtrada = new ArrayList<>();
        for (Comunidad c : listaOriginal) {
            if (c.getNombre() != null && c.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                filtrada.add(c);
            }
        }
        adapter.filtrar(filtrada);
    }

    private void limpiarLista() {
        listaComunidades.clear();
        listaOriginal.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void navegarAFragment(Fragment fragment) {
        // Verifica que R.id.fragment_container sea el ID de tu FrameLayout en activity_main.xml
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}