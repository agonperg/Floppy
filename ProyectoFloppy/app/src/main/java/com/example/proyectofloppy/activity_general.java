package com.example.proyectofloppy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;
import android.net.Uri;
import android.widget.Toast;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_general extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 0. Preparar el selector de archivos
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        subirArchivoACloudinary(uri);
                    } else {
                        Toast.makeText(this, "No se seleccionó ningún archivo", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // 1. INICIALIZAR CLOUDINARY
        try {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "dm9litchf");
            config.put("api_key", "418111144346765");
            config.put("api_secret", "bN71eKU8iT6oN8uhwZQM5x_qFN4");
            MediaManager.init(this, config);
        } catch (Exception e) {
            
        }

        if (savedInstanceState == null) {

            String origen = getIntent().getStringExtra("origen");

            Fragment fragmentoAMostrar;

            if ("desde_login".equals(origen)) {
                fragmentoAMostrar = new fragment_bienvenido();
            } else {
                fragmentoAMostrar = new rol();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentoAMostrar)
                    .commit();
        }
    }

    // Llama a esta función cuando quieras abrir la galería o selector de archivos
    public void openFileChooser() {
        // Usa "*/*" para cualquier archivo, o "image/*" para solo imágenes
        filePickerLauncher.launch("*/*"); 
    }

    private void subirArchivoACloudinary(Uri fileUri) {
        Toast.makeText(this, "Subiendo archivo a Cloudinary...", Toast.LENGTH_SHORT).show();

        MediaManager.get().upload(fileUri)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d("Cloudinary", "Comenzando subida...");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Opcional: Actualizar un ProgressBar si lo tuvieras en la UI
                        double progress = (double) bytes / totalBytes;
                        Log.d("Cloudinary", "Subiendo: " + (progress * 100) + "%");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String secureUrl = (String) resultData.get("secure_url");
                        Toast.makeText(activity_general.this, "Subida a Cloudinary exitosa!", Toast.LENGTH_SHORT).show();
                        
                        // Una vez subido exitosamente, guardamos en Firestore
                        guardarEnFirestore(secureUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(activity_general.this, "Error al subir a Cloudinary: " + error.getDescription(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.d("Cloudinary", "Subida reprogramada");
                    }
                }).dispatch();
    }

    private void guardarEnFirestore(String secureUrl) {
        // Obtener el ID del usuario si está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = (currentUser != null) ? currentUser.getUid() : "usuario_anonimo";

        // Crear mapa con los datos
        Map<String, Object> archivoInfo = new HashMap<>();
        archivoInfo.put("secure_url", secureUrl);
        archivoInfo.put("usuario_id", userId);
        archivoInfo.put("timestamp", com.google.firebase.firestore.FieldValue.serverTimestamp());

        // Guardar en la colección "usuarios_archivos"
        db.collection("usuarios_archivos")
                .add(archivoInfo)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(activity_general.this, "URL guardada en Firestore correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity_general.this, "Error guardando en Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
