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

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        fabAdd.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new fragment_crear_academia())
                    .addToBackStack(null)
                    .commit();
        });

        View cardAcademia1 = view.findViewById(R.id.card_academia_1);
        if (cardAcademia1 != null) {
            cardAcademia1.setOnClickListener(v -> navegarADetalle());
        }

        View cardAcademia2 = view.findViewById(R.id.card_academia_2);
        if (cardAcademia2 != null) {
            cardAcademia2.setOnClickListener(v -> navegarADetalle());
        }
    }

    private void navegarADetalle() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new fragment_detalle_academia())
                .addToBackStack(null)
                .commit();
    }
}