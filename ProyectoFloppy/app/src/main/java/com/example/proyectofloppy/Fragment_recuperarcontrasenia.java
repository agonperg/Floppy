package com.example.proyectofloppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class Fragment_recuperarcontrasenia extends Fragment {

    public Fragment_recuperarcontrasenia() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recuperarcontrasenia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etCorreo = view.findViewById(R.id.editTextText);
        Button btnEnviar = view.findViewById(R.id.button);
        TextView tvVolver = view.findViewById(R.id.textView4);

        btnEnviar.setOnClickListener(v -> {
            String email = etCorreo.getText().toString().trim();
            if (email.isEmpty()) {
                Snackbar.make(view, "Introduce tu correo electrónico", Snackbar.LENGTH_SHORT).show();
                return;
            }
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(view, "Enlace de recuperación enviado. Revisa tu correo.", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(view, "Error al enviar el enlace. Comprueba el correo.", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        });

        // "Volver al menú principal" cierra esta pantalla y regresa al Login
        tvVolver.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }
}
