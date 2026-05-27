package com.example.proyectofloppy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class activity_login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;

    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    private EditText etCorreo, etPassword;
    private Button btnLogin, btnOlvidoPass, btnIrRegistro;
    private MaterialButton btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("728124482870-fair3d4jb0glqeu7irssuoeb9tsejt7b.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Vistas
        etCorreo    = findViewById(R.id.correo);
        etPassword  = findViewById(R.id.password);
        btnLogin    = findViewById(R.id.button3);
        btnOlvidoPass = findViewById(R.id.button);
        btnIrRegistro = findViewById(R.id.button2);
        btnGoogle   = findViewById(R.id.btnGoogle);

        btnLogin.setOnClickListener(v -> iniciarSesion());

        btnIrRegistro.setOnClickListener(v -> {
            startActivity(new Intent(activity_login.this, activity_general.class));
        });

        btnOlvidoPass.setOnClickListener(v -> {
            Intent intent = new Intent(activity_login.this, activity_general.class);
            intent.putExtra("origen", "recuperar");
            startActivity(intent);
        });

        btnGoogle.setOnClickListener(v -> iniciarSesionGoogle());
    }

    // ── Login email/password ──────────────────────────────────────────────────

    private void iniciarSesion() {
        String email    = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            snack("Por favor, rellena correo y contraseña");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        irAGeneral();
                    } else {
                        snack("Error: Credenciales incorrectas");
                    }
                });
    }

    // ── Google Sign-In ────────────────────────────────────────────────────────

    private void iniciarSesionGoogle() {
        // Forzar selección de cuenta cada vez (opcional pero recomendable)
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthConGoogle(account.getIdToken());
            } catch (ApiException e) {
                snack("Error al iniciar con Google: " + e.getStatusCode());
            }
        }
    }

    private void firebaseAuthConGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        irAGeneral();
                    } else {
                        snack("Error al autenticar con Firebase");
                    }
                });
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void irAGeneral() {
        Intent intent = new Intent(activity_login.this, activity_general.class);
        intent.putExtra("origen", "desde_login");
        startActivity(intent);
        finish();
    }

    private void snack(String msg) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
    }

    // ── Recuperar contraseña ──────────────────────────────────────────────────

    private void mostrarDialogoRecuperar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar contraseña");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("Escribe tu correo");
        builder.setView(input);
        builder.setPositiveButton("Enviar", (dialog, i) -> {
            String email = input.getText().toString().trim();
            if (!email.isEmpty()) enviarCorreoRecuperacion(email);
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void enviarCorreoRecuperacion(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) snack("Correo de recuperación enviado");
                    else snack("Error al enviar el correo");
                });
    }
}