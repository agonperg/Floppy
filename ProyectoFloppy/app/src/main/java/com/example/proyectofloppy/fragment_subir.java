package com.example.proyectofloppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_subir extends Fragment {

    public fragment_subir() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos el nuevo layout
        return inflater.inflate(R.layout.fragment_subir, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Acción para el botón "Hecho"
        TextView btnHecho = view.findViewById(R.id.btn_hecho);
        btnHecho.setOnClickListener(v -> {
            Toast.makeText(getContext(), "¡Guardado con éxito!", Toast.LENGTH_SHORT).show();
        });

        // Acción para "Subir documentos"
        TextView btnSubir = view.findViewById(R.id.btn_subir_docs);
        btnSubir.setOnClickListener(v -> {
            // Aquí podrías abrir el selector de archivos
            Toast.makeText(getContext(), "Abriendo archivos...", Toast.LENGTH_SHORT).show();
        });
    }
}