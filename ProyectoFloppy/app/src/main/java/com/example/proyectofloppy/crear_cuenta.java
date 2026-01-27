package com.example.proyectofloppy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class crear_cuenta extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText etNombre, etApellidos, etCorreo, etPassword, etFecha;
    private TextView tvGrado, tvCurso;
    private View btnCrearCuenta;

    public crear_cuenta() {
    }

    public static crear_cuenta newInstance(String param1, String param2) {
        crear_cuenta fragment = new crear_cuenta();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.crear_cuenta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNombre = view.findViewById(R.id.etName);
        etApellidos = view.findViewById(R.id.etSurname);
        etCorreo = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etFecha = view.findViewById(R.id.etDate);

        tvGrado = view.findViewById(R.id.tvSelectorGrade);
        tvCurso = view.findViewById(R.id.tvSelectorCourse);

        btnCrearCuenta = view.findViewById(R.id.btnCreateAccount);

        configurarSelectorFecha();
        configurarSelectorGrado();
        configurarSelectorCurso();

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        final String nombre = etNombre.getText().toString().trim();
        final String apellidos = etApellidos.getText().toString().trim();
        final String email = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        final String fecha = etFecha.getText().toString().trim();
        final String grado = tvGrado.getText().toString();
        final String curso = tvCurso.getText().toString();

        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = auth.getCurrentUser().getUid();
                            guardarDatosEnFirestore(userId, nombre, apellidos, email, fecha, grado, curso);
                        } else {
                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void guardarDatosEnFirestore(String userId, String nombre, String apellidos, String email, String fecha, String grado, String curso) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", userId);
        usuario.put("nombre", nombre);
        usuario.put("apellidos", apellidos);
        usuario.put("email", email);
        usuario.put("fecha_nacimiento", fecha);
        usuario.put("grado", grado);
        usuario.put("curso", curso);

        db.collection("users").document(userId).set(usuario)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(getContext(), "Cuenta creada. Por favor, inicia sesión.", Toast.LENGTH_LONG).show();

                    auth.signOut();

                    if (getParentFragmentManager() != null) {
                        getParentFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al guardar perfil. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                    if (auth.getCurrentUser() != null) {
                        auth.getCurrentUser().delete();
                    }
                });
    }


    private void configurarSelectorFecha() {
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int yearActual = c.get(Calendar.YEAR);
                int monthActual = c.get(Calendar.MONTH);
                int dayActual = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String fechaSeleccionada = day + "/" + (month + 1) + "/" + year;
                                etFecha.setText(fechaSeleccionada);
                            }
                        }, yearActual, monthActual, dayActual);
                datePickerDialog.show();
            }
        });
    }

    private void configurarSelectorGrado() {
        final String[] opcionesGrado = {
                "Ingeniería Informática", "Medicina", "Derecho", "ADE",
                "Arquitectura", "Psicología", "Enfermería", "Economía"
        };

        tvGrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Selecciona tu grado");
                builder.setItems(opcionesGrado, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvGrado.setText(opcionesGrado[i]);
                    }
                });
                builder.show();
            }
        });
    }

    private void configurarSelectorCurso() {
        final String[] opcionesCurso = {"1º", "2º", "3º", "4º"};
        tvCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Selecciona tu curso");
                builder.setItems(opcionesCurso, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvCurso.setText(opcionesCurso[i]);
                    }
                });
                builder.show();
            }
        });
    }
}