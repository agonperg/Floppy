package com.example.proyectofloppy;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_buscarviaje extends Fragment {

    public Fragment_buscarviaje() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscarviaje, container, false);

        // Referencias
        EditText etOrigin = view.findViewById(R.id.etOrigin);
        EditText etDestination = view.findViewById(R.id.etDestination);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        TextView tvPublish = view.findViewById(R.id.tvPublish);
        View btnBack = view.findViewById(R.id.btnBack);

        // ¡OJO! Asegúrate de que en el XML el contenedor de las cards tenga id: linearLayoutResultados
        LinearLayout resultadosContainer = view.findViewById(R.id.linearLayoutResultados);

        btnSearch.setOnClickListener(v -> {
            if (etOrigin.getText().toString().isEmpty() || etDestination.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Introduce origen y destino", Toast.LENGTH_SHORT).show();
            } else if (resultadosContainer != null) {
                resultadosContainer.setVisibility(View.VISIBLE);
            }
        });

        tvPublish.setOnClickListener(v -> navigateTo(new Fragment_publicarviaje()));

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Asegúrate de que la CardView de Guillermo tenga id: cardGuillermo en el XML
        View cardGuillermo = view.findViewById(R.id.cardGuillermo);
        if (cardGuillermo != null) {
            cardGuillermo.setOnClickListener(v -> navigateTo(new Fragment_detallesviaje()));
        }

        return view;
    }

    // ESTE MÉTODO ES EL QUE TE FALTABA AL FINAL DE LA CLASE
    private void navigateTo(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}