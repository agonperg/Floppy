package com.example.proyectofloppy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class activity_general extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        // Si es la primera vez que se crea la actividad, cargamos el fragmento de bienvenida
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new fragment_bienvenido())
                    .commit();
        }
    }
}