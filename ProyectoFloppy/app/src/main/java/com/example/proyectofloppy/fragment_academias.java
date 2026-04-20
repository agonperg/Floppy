package com.example.proyectofloppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_academias extends Fragment {

    public fragment_academias() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_academias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView btnBack = view.findViewById(R.id.btn_back_academias);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
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