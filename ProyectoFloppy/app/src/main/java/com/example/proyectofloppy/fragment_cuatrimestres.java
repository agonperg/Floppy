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
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            });
        }

        ImageView folder1 = view.findViewById(R.id.folder_1);
        if (folder1 != null) {
            folder1.setOnClickListener(v -> irATemas("1"));
        }

        ImageView folder2 = view.findViewById(R.id.folder_2);
        if (folder2 != null) folder2.setOnClickListener(v -> irATemas("2"));

        ImageView folder3 = view.findViewById(R.id.folder_3);
        if (folder3 != null) folder3.setOnClickListener(v -> irATemas("3"));

    }

    // Método auxiliar para no repetir código
    private void irATemas(String cuatrimestre) {
        fragment_temas fragmentTemas = new fragment_temas();
        Bundle args = new Bundle();
        args.putString("cuatrimestre_seleccionado", cuatrimestre);
        fragmentTemas.setArguments(args);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentTemas)
                .addToBackStack(null)
                .commit();
    }
}