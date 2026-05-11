package com.example.proyectofloppy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_general extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<String> filePickerLauncher;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        bottomNav = findViewById(R.id.bottom_navigation);

        // Prepare file picker
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

        // Initialize Cloudinary
        try {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "dm9litchf");
            config.put("api_key", "418111144346765");
            config.put("api_secret", "bN71eKU8iT6oN8uhwZQM5x_qFN4");
            MediaManager.init(this, config);
        } catch (Exception e) {
        }

        // Configure BottomNavigationView tab listener
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;

            if (itemId == R.id.nav_inicio) {
                selectedFragment = new fragment_bienvenido();
            } else if (itemId == R.id.nav_comunidades) {
                selectedFragment = new fragment_comunidades();
            } else if (itemId == R.id.nav_transporte) {
                selectedFragment = new Fragment_buscarviaje();
            } else if (itemId == R.id.nav_academias) {
                selectedFragment = new fragment_academias();
            } else if (itemId == R.id.nav_apuntes) {
                selectedFragment = new fragment_parati();
            }

            if (selectedFragment != null) {
                // Clear backstack when switching tabs
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            String origen = getIntent().getStringExtra("origen");

            if ("desde_login".equals(origen)) {
                // Main app flow: show home, show BottomNav
                bottomNav.setVisibility(View.VISIBLE);
                bottomNav.setSelectedItemId(R.id.nav_inicio);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, new fragment_bienvenido())
                        .commit();
            } else if ("recuperar".equals(origen)) {
                // Password recovery flow: hide BottomNav, show recovery screen
                bottomNav.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, new Fragment_recuperarcontrasenia())
                        .commit();
            } else {
                // Registration flow: hide BottomNav, show role selection
                bottomNav.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, new rol())
                        .commit();
            }
        }
    }

    public BottomNavigationView getBottomNav() {
        return bottomNav;
    }

    public void showBottomNav() {
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }

    public void openFileChooser() {
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
                        double progress = (double) bytes / totalBytes;
                        Log.d("Cloudinary", "Subiendo: " + (progress * 100) + "%");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String secureUrl = (String) resultData.get("secure_url");
                        Toast.makeText(activity_general.this, "Subida a Cloudinary exitosa!", Toast.LENGTH_SHORT).show();
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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = (currentUser != null) ? currentUser.getUid() : "usuario_anonimo";

        Map<String, Object> archivoInfo = new HashMap<>();
        archivoInfo.put("secure_url", secureUrl);
        archivoInfo.put("usuario_id", userId);
        archivoInfo.put("timestamp", com.google.firebase.firestore.FieldValue.serverTimestamp());

        db.collection("usuarios_archivos")
                .add(archivoInfo)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(activity_general.this, "URL guardada en Firestore correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity_general.this, "Error guardando en Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
    // Añade esto dentro de activity_general.java
    public void setBottomNavigationVisibility(int visibility) {
        if (bottomNav != null) {
            bottomNav.setVisibility(visibility);
        }
    }
}
