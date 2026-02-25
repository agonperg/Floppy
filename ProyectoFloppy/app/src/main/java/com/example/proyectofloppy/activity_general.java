package com.example.proyectofloppy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class activity_general extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        // Le decimos a Android que meta tu Fragment_comunidades en el hueco
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_fragments, new fragment_comunidades())
                    .commit();
        }
    }
}