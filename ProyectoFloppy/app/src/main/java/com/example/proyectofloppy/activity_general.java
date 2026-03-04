package com.example.proyectofloppy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

public class activity_general extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

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
}