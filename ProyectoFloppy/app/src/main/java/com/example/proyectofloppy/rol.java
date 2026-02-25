package com.example.proyectofloppy;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.material.snackbar.Snackbar;

public class rol extends Fragment {

    private CardView cardEstudiante, cardDocente;
    private Button btnContinuar;
    private String seleccion = "";

    public rol() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rol, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardEstudiante = view.findViewById(R.id.cardEstudiante);
        cardDocente = view.findViewById(R.id.cardDocente);
        btnContinuar = view.findViewById(R.id.btnContinuar);

        cardEstudiante.setOnClickListener(v -> {
            seleccion = "estudiante";
            marcarSeleccion();
        });

        cardDocente.setOnClickListener(v -> {
            seleccion = "docente";
            marcarSeleccion();
        });

        btnContinuar.setOnClickListener(v -> {
            if (seleccion.isEmpty()) {
                Snackbar.make(view, "Por favor, selecciona una opción para continuar", Snackbar.LENGTH_SHORT).show();
                return;
            }

            Fragment destino;
            if (seleccion.equals("estudiante")) {
                destino = new crear_cuenta();
            } else {
                destino = new crear_cuenta_docente();
            }

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, destino)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void marcarSeleccion() {
        if (seleccion.equals("estudiante")) {
            cardEstudiante.setCardBackgroundColor(Color.parseColor("#D9E8F5"));
            cardDocente.setCardBackgroundColor(Color.parseColor("#E8E2EA"));
        } else {
            cardDocente.setCardBackgroundColor(Color.parseColor("#D9E8F5"));
            cardEstudiante.setCardBackgroundColor(Color.parseColor("#E8E2EA"));
        }
    }
}