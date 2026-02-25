package com.example.proyectofloppy;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class intereses extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private String nombre, apellidos, email, password, fecha, grado, curso, telefono, direccion, especializacion, website;
    private boolean esDocente = false;
    private List<String> interesesSeleccionados = new ArrayList<>();

    private ImageView btnVolver;

    public intereses() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            esDocente = getArguments().getBoolean("esDocente", false);
            email = getArguments().getString("email");
            password = getArguments().getString("password");
            nombre = getArguments().getString("nombre");

            if (esDocente) {
                telefono = getArguments().getString("telefono");
                direccion = getArguments().getString("direccion");
                especializacion = getArguments().getString("especializacion");
                website = getArguments().getString("website");
            } else {
                apellidos = getArguments().getString("apellidos");
                fecha = getArguments().getString("fecha_nacimiento");
                grado = getArguments().getString("grado");
                curso = getArguments().getString("curso");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intereses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnVolver = view.findViewById(R.id.btnVolver);

        configurarTarjeta(view.findViewById(R.id.cardDeportes), "Deportes");
        configurarTarjeta(view.findViewById(R.id.cardCiencia), "Ciencia");
        configurarTarjeta(view.findViewById(R.id.cardIngenieria), "Ingeniería");
        configurarTarjeta(view.findViewById(R.id.cardMatematicas), "Matemáticas");
        configurarTarjeta(view.findViewById(R.id.cardTecnologia), "Tecnología");
        configurarTarjeta(view.findViewById(R.id.cardProgramacion), "Programación");
        configurarTarjeta(view.findViewById(R.id.cardArte), "Arte");
        configurarTarjeta(view.findViewById(R.id.cardDiseno), "Diseño");
        configurarTarjeta(view.findViewById(R.id.cardHumanidades), "Humanidades");
        configurarTarjeta(view.findViewById(R.id.cardComunicacion), "Comunicación");
        configurarTarjeta(view.findViewById(R.id.cardPsicologia), "Psicología");
        configurarTarjeta(view.findViewById(R.id.cardMedicina), "Medicina");
        configurarTarjeta(view.findViewById(R.id.cardDerecho), "Derecho");
        configurarTarjeta(view.findViewById(R.id.cardEconomia), "Economía");
        configurarTarjeta(view.findViewById(R.id.cardMarketing), "Marketing");
        configurarTarjeta(view.findViewById(R.id.cardEducacion), "Educación");
        configurarTarjeta(view.findViewById(R.id.cardArquitectura), "Arquitectura");
        configurarTarjeta(view.findViewById(R.id.cardMusica), "Música");

        MaterialButton btnSubmit = view.findViewById(R.id.btnSubmitInterests);

        btnSubmit.setOnClickListener(v -> registrarUsuarioCompleto());

        btnVolver.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void configurarTarjeta(MaterialCardView card, String interes) {
        card.setOnClickListener(v -> {
            if (interesesSeleccionados.contains(interes)) {
                interesesSeleccionados.remove(interes);
                card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                card.setStrokeColor(Color.parseColor("#CCCCCC"));
            } else {
                interesesSeleccionados.add(interes);
                card.setCardBackgroundColor(Color.parseColor("#D9E8F5"));
                card.setStrokeColor(Color.parseColor("#0E4A81"));
            }
        });
    }

    private void registrarUsuarioCompleto() {
        if (interesesSeleccionados.isEmpty()) {
            Snackbar.make(requireView(), "Selecciona al menos un interés", Snackbar.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();
                        guardarDatosFirestore(userId);
                    } else {
                        Snackbar.make(requireView(), "Error: " + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void guardarDatosFirestore(String userId) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", userId);
        usuario.put("email", email);
        usuario.put("intereses", interesesSeleccionados);
        usuario.put("rol", esDocente ? "docente" : "estudiante");

        if (esDocente) {
            usuario.put("nombre_academia", nombre);
            usuario.put("telefono", telefono);
            usuario.put("direccion", direccion);
            usuario.put("especializacion", especializacion);
            usuario.put("website", website);
        } else {
            usuario.put("nombre", nombre);
            usuario.put("apellidos", apellidos);
            usuario.put("fecha_nacimiento", fecha);
            usuario.put("grado", grado);
            usuario.put("curso", curso);
        }

        db.collection("users").document(userId).set(usuario)
                .addOnSuccessListener(unused -> {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), "Cuenta creada con éxito", Snackbar.LENGTH_LONG).show();
                    auth.signOut();
                    requireActivity().finish();
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(requireView(), "Error al guardar perfil", Snackbar.LENGTH_SHORT).show();
                    if (auth.getCurrentUser() != null) {
                        auth.getCurrentUser().delete();
                    }
                });
    }
}