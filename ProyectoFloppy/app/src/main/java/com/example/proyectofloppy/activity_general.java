package com.example.proyectofloppy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class activity_general extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, new Fragment_buscarviaje())
                    .commit();
        }
    }
}