package com.example.proyectofloppy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofloppy.Academia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

public class fragment_academias extends Fragment {

    private RecyclerView rvAcademias;
    private AcademiaAdapter adapter;
    private List<Academia> academiaList;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabAdd;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public fragment_academias() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_academias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvAcademias = view.findViewById(R.id.rv_academias);
        fabAdd = view.findViewById(R.id.fab_add_academia);
        ImageView btnBack = view.findViewById(R.id.btn_back_academias);

        academiaList = new ArrayList<>();
        adapter = new AcademiaAdapter(academiaList);
        rvAcademias.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAcademias.setAdapter(adapter);

        // Configurar el listener de eliminación
        adapter.setOnDeleteClickListener(this::mostrarConfirmacionBorrado);

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        fabAdd.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new fragment_crear_academia())
                    .addToBackStack(null)
                    .commit();
        });

        comprobarRolYCargarDatos();
    }

    private void mostrarConfirmacionBorrado(Academia academia) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Academia")
                .setMessage("¿Estás seguro de que deseas eliminar la academia \"" + academia.getNombre() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarAcademia(academia))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarAcademia(Academia academia) {
        db.collection("academias").document(academia.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Academia eliminada con éxito", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void comprobarRolYCargarDatos() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // 1. Comprobar ROL
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String rol = documentSnapshot.getString("rol");
                            if ("docente".equals(rol)) {
                                fabAdd.setVisibility(View.VISIBLE);
                                adapter.setDocente(true);
                            }
                        }
                    });

            // 2. Cargar ACADEMIAS
            db.collection("academias").orderBy("timestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.e("Firestore", "Error al cargar academias", error);
                            return;
                        }

                        if (value != null) {
                            academiaList.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                Academia academia = doc.toObject(Academia.class);
                                academiaList.add(academia);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }
}