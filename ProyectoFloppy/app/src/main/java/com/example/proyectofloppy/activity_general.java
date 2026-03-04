package com.example.proyectofloppy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class activity_general extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        // 1. INICIALIZAR CLOUDINARY
        try {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "dm9litchf"); // <-- REEMPLAZA ESTO
            config.put("api_key", "418111144346765");       // <-- REEMPLAZA ESTO
            config.put("api_secret", "bN71eKU8iT6oN8uhwZQM5x_qFN4"); // <-- REEMPLAZA ESTO
            MediaManager.init(this, config);
        } catch (Exception e) {
            // Esto evita que la app falle si rotas la pantalla y se intenta inicializar 2 veces
        }

        // 2. CARGAR EL FRAGMENTO INICIAL
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new fragment_parati())
                    .commit();
        }
    }
}