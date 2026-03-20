package com.example.proyectofloppy;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Fragment_detallesviaje extends Fragment {

    public Fragment_detallesviaje() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detallesviaje, container, false);

        Button btnReservar = view.findViewById(R.id.btnReservar);
        View btnBack = view.findViewById(R.id.btnBack);

        // Toast de reserva
        btnReservar.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Plaza reservada", Toast.LENGTH_SHORT).show();
        });

        // Volver atrás
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }
}