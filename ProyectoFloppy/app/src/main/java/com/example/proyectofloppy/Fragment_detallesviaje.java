package com.example.proyectofloppy;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_detallesviaje extends Fragment {

    public Fragment_detallesviaje() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detallesviaje, container, false);

        TextView tvRuta = view.findViewById(R.id.tvRuta);
        TextView tvFecha = view.findViewById(R.id.tvFechaDetalle);
        TextView tvHora = view.findViewById(R.id.tvHoraDetalle);
        TextView tvPlazas = view.findViewById(R.id.tvPlazasDetalle);
        TextView tvConductor = view.findViewById(R.id.tvNombreConductor);
        TextView tvPrecio = view.findViewById(R.id.tvPrecioViaje);
        Button btnReservar = view.findViewById(R.id.btnReservar);
        View btnBack = view.findViewById(R.id.btnBack);

        Bundle args = getArguments();
        if (args != null) {
            String origen = args.getString("origen", "");
            String destino = args.getString("destino", "");
            tvRuta.setText(origen + " → " + destino);
            tvFecha.setText("Fecha: " + args.getString("fecha", "—"));
            tvHora.setText("Hora: " + args.getString("hora", "—") + "h");
            tvPlazas.setText("Plazas: " + args.getString("plazas", "—"));
            tvPrecio.setText(args.getString("precio", "—") + "€/persona");
            tvConductor.setText(args.getString("conductor", "—"));
        }

        btnReservar.setOnClickListener(v ->
                Toast.makeText(getContext(), "Plaza reservada", Toast.LENGTH_SHORT).show());

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        return view;
    }
}
