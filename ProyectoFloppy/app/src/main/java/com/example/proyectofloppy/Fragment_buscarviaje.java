package com.example.proyectofloppy;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class Fragment_buscarviaje extends Fragment {

    private ViajeAdapter adapter;
    private final List<Viaje> listaViajes = new ArrayList<>();

    public Fragment_buscarviaje() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscarviaje, container, false);

        EditText etOrigin = view.findViewById(R.id.etOrigin);
        EditText etDestination = view.findViewById(R.id.etDestination);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        TextView tvPublish = view.findViewById(R.id.tvPublish);
        View btnBack = view.findViewById(R.id.btnBack);
        RecyclerView rvViajes = view.findViewById(R.id.rvViajes);
        TextView tvNoResults = view.findViewById(R.id.tvNoResults);

        adapter = new ViajeAdapter(listaViajes);
        rvViajes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvViajes.setAdapter(adapter);

        adapter.setOnItemClickListener(viaje -> {
            Fragment_detallesviaje detalle = new Fragment_detallesviaje();
            Bundle args = new Bundle();
            args.putString("origen", viaje.getOrigen());
            args.putString("destino", viaje.getDestino());
            args.putString("fecha", viaje.getFecha());
            args.putString("hora", viaje.getHora());
            args.putString("plazas", String.valueOf(viaje.getPlazas()));
            args.putString("precio", String.valueOf(viaje.getPrecio()));
            args.putString("conductor", viaje.getConductorNombre());
            detalle.setArguments(args);
            
            // Registrar visita para el historial reciente
            VisitManager.registrarVisita("viaje", viaje.getId(), viaje.getOrigen() + " - " + viaje.getDestino());

            navigateTo(detalle);
        });

        btnSearch.setOnClickListener(v -> {
            String origen = etOrigin.getText().toString().trim();
            String destino = etDestination.getText().toString().trim();
            if (origen.isEmpty() || destino.isEmpty()) {
                Toast.makeText(getContext(), "Introduce origen y destino", Toast.LENGTH_SHORT).show();
                return;
            }
            buscarViajes(origen, destino, tvNoResults);
        });

        tvPublish.setOnClickListener(v -> navigateTo(new Fragment_publicarviaje()));

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        return view;
    }

    private void buscarViajes(String origen, String destino, TextView tvNoResults) {
        // Filtra por origen en Firestore y por destino en cliente para evitar índice compuesto
        FirebaseFirestore.getInstance().collection("viajes")
                .whereEqualTo("origen", origen)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaViajes.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Viaje v = doc.toObject(Viaje.class);
                        v.setId(doc.getId());
                        if (destino.equalsIgnoreCase(v.getDestino())) {
                            listaViajes.add(v);
                        }
                    }
                    adapter.actualizar(listaViajes);
                    tvNoResults.setVisibility(listaViajes.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error en la búsqueda", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateTo(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
