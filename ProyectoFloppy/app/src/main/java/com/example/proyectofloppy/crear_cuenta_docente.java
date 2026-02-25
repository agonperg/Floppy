package com.example.proyectofloppy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class crear_cuenta_docente extends Fragment {

    private EditText etAcademyName, etEmail, etPassword, etPhone, etAddress, etWebsite;
    private TextView tvSelectorSpecialization, tvLoginLink;
    private View btnCreateAccount;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public crear_cuenta_docente() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_cuenta_docente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etAcademyName = view.findViewById(R.id.etAcademyName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etPhone = view.findViewById(R.id.etPhone);
        etAddress = view.findViewById(R.id.etAddress);
        etWebsite = view.findViewById(R.id.etWebsite);
        tvSelectorSpecialization = view.findViewById(R.id.tvSelectorSpecialization);
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        tvLoginLink = view.findViewById(R.id.tvLoginLink);

        configurarSelectorEspecializacion();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarEmpresaDirecto();
            }
        });

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().finish();
            }
        });
    }

    private void configurarSelectorEspecializacion() {
        final String[] especialidades = {
                "Refuerzo Escolar", "Idiomas", "Música", "Oposiciones",
                "Programación", "Arte y Dibujo", "Deportes"
        };

        tvSelectorSpecialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Áreas de especialización");
                builder.setItems(especialidades, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvSelectorSpecialization.setText(especialidades[i]);
                    }
                });
                builder.show();
            }
        });
    }

    private void registrarEmpresaDirecto() {
        String nombre = etAcademyName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String telf = etPhone.getText().toString().trim();
        String direccion = etAddress.getText().toString().trim();
        String especialidad = tvSelectorSpecialization.getText().toString().trim();
        String web = etWebsite.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty() || telf.isEmpty() || direccion.isEmpty() || especialidad.isEmpty() || especialidad.equals("Área/as de especialización")) {
            Snackbar.make(requireView(), "Por favor, rellena todos los campos obligatorios", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(requireView(), "Correo no válido", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Snackbar.make(requireView(), "Contraseña corta (mín. 6 caracteres)", Snackbar.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();

                        Map<String, Object> usuario = new HashMap<>();
                        usuario.put("id", userId);
                        usuario.put("email", email);
                        usuario.put("nombre_academia", nombre);
                        usuario.put("telefono", telf);
                        usuario.put("direccion", direccion);
                        usuario.put("especializacion", especialidad);
                        usuario.put("website", web);
                        usuario.put("rol", "docente");

                        db.collection("users").document(userId).set(usuario)
                                .addOnSuccessListener(unused -> {
                                    auth.signOut();
                                    if (getActivity() != null) {
                                        requireActivity().finish();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Snackbar.make(requireView(), "Error al guardar perfil", Snackbar.LENGTH_SHORT).show();
                                });

                    } else {
                        Snackbar.make(requireView(), "Error: " + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }
}