package com.example.proyectofloppy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Importaciones de Cloudinary y Firestore
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class fragment_subir extends Fragment {

    private Uri archivoUri = null;
    private ActivityResultLauncher<Intent> filePickerLauncher;

    public fragment_subir() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Selector de archivos PDF
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        archivoUri = result.getData().getData();
                        Toast.makeText(getContext(), "Archivo PDF seleccionado", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subir, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etNombre = view.findViewById(R.id.et_nombre_apuntes);
        EditText etAsignatura = view.findViewById(R.id.et_asignatura);
        EditText etCuatrimestre = view.findViewById(R.id.et_carrera);

        TextView btnSubir = view.findViewById(R.id.btn_subir_docs);
        btnSubir.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            filePickerLauncher.launch(intent);
        });

        TextView btnHecho = view.findViewById(R.id.btn_hecho);
        btnHecho.setOnClickListener(v -> {
            if (archivoUri == null) {
                Toast.makeText(getContext(), "Por favor, selecciona un documento primero", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombre.getText().toString();
            String asignatura = etAsignatura.getText().toString();
            String cuatrimestre = etCuatrimestre.getText().toString();

            // Llamamos al nuevo método de Cloudinary
            subirACloudinary(nombre, asignatura, cuatrimestre, archivoUri);
        });
    }

    private void subirACloudinary(String nombre, String asignatura, String cuatri, Uri uri) {
        Toast.makeText(getContext(), "Subiendo apunte... Por favor, espera.", Toast.LENGTH_LONG).show();

        MediaManager.get().upload(uri)
                .option("resource_type", "auto") // vital para PDFs
                .unsigned("floppy_preset")       // <-- CAMBIA ESTO por el nombre exacto de tu preset
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d("Cloudinary", "Comenzando subida...");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Aquí el archivo se está subiendo
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        // 1. Cloudinary nos da el enlace seguro
                        String urlPdf = (String) resultData.get("secure_url");
                        Log.d("Cloudinary", "Subida exitosa: " + urlPdf);

                        // 2. Guardamos todo en Firestore
                        guardarEnFirestore(nombre, asignatura, cuatri, urlPdf);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.e("Cloudinary", "Error al subir: " + error.getDescription());
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Error subiendo a Cloudinary", Toast.LENGTH_SHORT).show()
                            );
                        }
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void guardarEnFirestore(String nombre, String asignatura, String cuatri, String url) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> apunte = new HashMap<>();
        apunte.put("nombre", nombre);
        apunte.put("asignatura", asignatura);
        apunte.put("cuatrimestre", cuatri);
        apunte.put("url", url);

        db.collection("Apuntes").add(apunte).addOnSuccessListener(documentReference -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "¡Apunte guardado y publicado!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack(); // Volver a la pantalla anterior
                });
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error guardando en BD: " + e.getMessage());
        });
    }
}