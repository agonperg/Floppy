package com.example.proyectofloppy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;

public class Fragment_publicarviaje extends Fragment {

    private int plazas = 1;
    private String fechaSeleccionada = "";
    private String horaSeleccionada = "";

    public Fragment_publicarviaje() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publicarviaje, container, false);

        EditText etOrigen = view.findViewById(R.id.etOrigenPublicar);
        EditText etDestino = view.findViewById(R.id.etDestinoPublicar);
        EditText etPrecio = view.findViewById(R.id.etPrecioPublicar);
        TextView tvFecha = view.findViewById(R.id.tvFechaSelec);
        TextView tvHora = view.findViewById(R.id.tvHoraSelec);
        TextView tvPlazasNum = view.findViewById(R.id.tvPlazasNum);
        Button btnMas = view.findViewById(R.id.btnMas);
        Button btnMenos = view.findViewById(R.id.btnMenos);
        Button btnPublicar = view.findViewById(R.id.btnPublicar);
        View btnBack = view.findViewById(R.id.btnBack);

        tvPlazasNum.setText(String.valueOf(plazas));

        view.findViewById(R.id.boxDate).setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (dp, year, month, day) -> {
                fechaSeleccionada = String.format("%02d/%02d/%d", day, month + 1, year);
                tvFecha.setText(fechaSeleccionada);
                tvFecha.setTextColor(0xFF000000);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        view.findViewById(R.id.boxTime).setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(requireContext(), (tp, hour, minute) -> {
                horaSeleccionada = String.format("%02d:%02d", hour, minute);
                tvHora.setText(horaSeleccionada);
                tvHora.setTextColor(0xFF000000);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        });

        btnMas.setOnClickListener(v -> {
            plazas++;
            tvPlazasNum.setText(String.valueOf(plazas));
        });

        btnMenos.setOnClickListener(v -> {
            if (plazas > 1) {
                plazas--;
                tvPlazasNum.setText(String.valueOf(plazas));
            }
        });

        btnPublicar.setOnClickListener(v -> publicarViaje(etOrigen, etDestino, etPrecio));

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        return view;
    }

    private void publicarViaje(EditText etOrigen, EditText etDestino, EditText etPrecio) {
        String origen = etOrigen.getText().toString().trim();
        String destino = etDestino.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        if (origen.isEmpty() || destino.isEmpty() || fechaSeleccionada.isEmpty()
                || horaSeleccionada.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Precio no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = user.getDisplayName() != null ? user.getDisplayName() : user.getEmail();
        Viaje viaje = new Viaje(origen, destino, fechaSeleccionada, horaSeleccionada,
                precio, plazas, user.getUid(), nombre);

        FirebaseFirestore.getInstance().collection("viajes")
                .add(viaje)
                .addOnSuccessListener(docRef -> {
                    Log.d("Floppy", "Viaje guardado con ID: " + docRef.getId());
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Viaje publicado con éxito", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Floppy", "Error al guardar viaje en Firestore", e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
