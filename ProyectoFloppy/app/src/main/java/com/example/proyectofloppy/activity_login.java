package com.example.proyectofloppy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment; // Añadido para manejar el Fragment

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_login extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText etCorreo, etPassword;
    private Button btnLogin, btnOlvidoPass, btnIrRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        auth.signOut();
        etCorreo = findViewById(R.id.correo);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.button3);
        btnOlvidoPass = findViewById(R.id.button);
        btnIrRegistro = findViewById(R.id.button2);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });

        btnIrRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(activity_login.this, activity_general.class);
                startActivity(intent);
            }
        });

        btnOlvidoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoRecuperar();
            }
        });
    }

    private void iniciarSesion() {
        String email = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Por favor, rellena correo y contraseña", Snackbar.LENGTH_SHORT).show();
            return;
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(findViewById(android.R.id.content), "LOGIN CORRECTO", Snackbar.LENGTH_SHORT).show();

                            android.content.Intent intent = new android.content.Intent(activity_login.this, activity_general.class);

                            intent.putExtra("origen", "desde_login");

                            startActivity(intent);
                            finish();

                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Error: Credenciales incorrectas", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void mostrarDialogoRecuperar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar contraseña");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("Escribe tu correo");
        builder.setView(input);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = input.getText().toString().trim();
                if (!email.isEmpty()) {
                    enviarCorreoRecuperacion(email);
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void enviarCorreoRecuperacion(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(findViewById(android.R.id.content), "Correo de recuperación enviado", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Error al enviar el correo", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}