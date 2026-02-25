package com.example.proyectofloppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_cuatrimestres extends Fragment {

    public fragment_cuatrimestres() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cuatrimestres, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Botón atrás
        ImageView btnBack = view.findViewById(R.id.btn_back_cuatrimestre);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        // Ejemplo de clic en una carpeta
        ImageView folder1 = view.findViewById(R.id.folder_1);
        folder1.setOnClickListener(v -> {
            // Aquí navegarías a la lista de asignaturas de ese cuatrimestre
        });
    }
}