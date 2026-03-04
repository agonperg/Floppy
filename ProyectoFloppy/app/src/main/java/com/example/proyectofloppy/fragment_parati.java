package com.example.proyectofloppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_parati extends Fragment {

    public fragment_parati() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos el layout fragment_parati.xml
        return inflater.inflate(R.layout.fragment_parati, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configuración del botón atrás
        ImageView btnBack = view.findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            });
        }

        // Configuración del botón publicar
        View btnPublicar = view.findViewById(R.id.btn_publicar);
        if (btnPublicar != null) {
            btnPublicar.setOnClickListener(v -> {
                // Aquí hacemos la magia: Navegar a la pantalla de subir
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new fragment_subir()) // Asegúrate de que fragment_container es tu ID real
                        .addToBackStack(null) // Esto permite volver atrás con el botón del móvil
                        .commit();
            });
        }
    }
}