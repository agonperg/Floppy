package com.example.proyectofloppy;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_publicarviaje extends Fragment {

    private int plazas = 1;

    public Fragment_publicarviaje() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publicarviaje, container, false);

        // Referencias - Revisa que estos IDs existan en fragment_publicarviaje.xml
        TextView tvPlazasNum = view.findViewById(R.id.tvPlazasNum);
        Button btnMas = view.findViewById(R.id.btnMas);
        Button btnMenos = view.findViewById(R.id.btnMenos);
        Button btnPublicar = view.findViewById(R.id.btnPublicar);
        View btnBack = view.findViewById(R.id.btnBack);

        if (btnMas != null) {
            btnMas.setOnClickListener(v -> {
                plazas++;
                tvPlazasNum.setText(String.valueOf(plazas));
            });
        }

        if (btnMenos != null) {
            btnMenos.setOnClickListener(v -> {
                if (plazas > 1) {
                    plazas--;
                    tvPlazasNum.setText(String.valueOf(plazas));
                }
            });
        }

        if (btnPublicar != null) {
            btnPublicar.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Viaje publicado con éxito", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            });
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        return view;
    }
}