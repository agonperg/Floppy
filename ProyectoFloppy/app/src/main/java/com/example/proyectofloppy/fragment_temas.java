package com.example.proyectofloppy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class fragment_temas extends Fragment {

    private FirebaseFirestore db;
    private String cuatrimestreSeleccionado = "1";
    private GridLayout gridTemas;

    public fragment_temas() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Botón Atrás
        ImageView btnBack = view.findViewById(R.id.btn_back_temas);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            });
        }

        // 2. Referencia al GridLayout
        gridTemas = view.findViewById(R.id.grid_temas);

        // 3. Obtener el cuatrimestre seleccionado
        if (getArguments() != null) {
            cuatrimestreSeleccionado = getArguments().getString("cuatrimestre_seleccionado", "1");
        }

        // 4. Buscar en Firebase
        cargarApuntesDesdeFirestore();
    }

    private void cargarApuntesDesdeFirestore() {
        db = FirebaseFirestore.getInstance();

        // Limpiamos la cuadrícula por si acaso
        gridTemas.removeAllViews();

        db.collection("Apuntes")
                .whereEqualTo("cuatrimestre", cuatrimestreSeleccionado)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombre = document.getString("nombre");
                            String url = document.getString("url");

                            // Dibuja el icono del PDF y lo mete en la cuadrícula
                            agregarArchivoAGrid(nombre, url);
                        }
                    } else {
                        Toast.makeText(getContext(), "Error al cargar los documentos", Toast.LENGTH_SHORT).show();
                        Log.e("Firebase", "Error", task.getException());
                    }
                });
    }

    private void agregarArchivoAGrid(String nombre, String url) {
        // Inflamos nuestro diseño individual (item_pdf)
        View itemPdf = LayoutInflater.from(getContext()).inflate(R.layout.item_pdf, gridTemas, false);

        // Le ponemos el nombre que viene de Firebase
        TextView tvNombre = itemPdf.findViewById(R.id.tv_nombre_archivo);
        if (nombre != null) {
            tvNombre.setText(nombre);
        }

        // Le damos acción al hacer clic
        itemPdf.setOnClickListener(v -> descargarDocumento(url));

        // Para que se distribuya uniformemente en el GridLayout (como tenías layout_columnWeight)
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Peso 1
        itemPdf.setLayoutParams(params);

        // Lo añadimos al GridLayout en la pantalla
        gridTemas.addView(itemPdf);
    }

    private void descargarDocumento(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "El documento no tiene una URL válida", Toast.LENGTH_SHORT).show();
        }
    }
}