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
        // Constructor vacío requerido por Android
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos el diseño visual (XML) de esta pantalla
        return inflater.inflate(R.layout.fragment_parati, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- 1. CONFIGURACIÓN DEL BOTÓN ATRÁS (AHORA LLEVA A LOS APUNTES) ---
        ImageView btnBack = view.findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getParentFragmentManager() != null) {
                    // Magia aquí: En lugar de retroceder, abrimos fragment_temas directamente
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new fragment_temas())
                            .addToBackStack(null) // Para que el botón físico del móvil no cierre la app de golpe
                            .commit();
                }
            });
        }

        // --- 2. CONFIGURACIÓN DEL BOTÓN PUBLICAR ---
        View btnPublicar = view.findViewById(R.id.btn_publicar);
        if (btnPublicar != null) {
            btnPublicar.setOnClickListener(v -> {
                if (getParentFragmentManager() != null) {
                    // Hacemos el cambio de pantalla a fragment_subir
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new fragment_subir())
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}