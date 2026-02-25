package com.example.proyectofloppy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class crear_cuenta extends Fragment {

    private EditText etNombre, etApellidos, etCorreo, etPassword, etFecha;
    private TextView tvGrado, tvCurso;
    private View btnCrearCuenta, btnVolverLogin;

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
        btnVolverLogin = view.findViewById(R.id.btnVolverLogin);

        configurarSelectorFecha();
        configurarSelectorGrado();
        configurarSelectorCurso();

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAPantallaIntereses();
            }
        });

        btnVolverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    private void irAPantallaIntereses() {
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String email = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();
        String grado = tvGrado.getText().toString().trim();
        String curso = tvCurso.getText().toString().trim();

        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty() || fecha.isEmpty() || grado.isEmpty() || grado.equals("Selecciona tu grado") || curso.isEmpty() || curso.equals("Selecciona tu curso actual") || curso.equals("Selecciona tu curso")) {
            com.google.android.material.snackbar.Snackbar.make(requireView(), "Por favor, rellena todos los campos", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(requireView(), "Por favor, introduce un correo electrónico válido", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Snackbar.make(requireView(), "La contraseña debe tener al menos 6 caracteres", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("nombre", nombre);
        bundle.putString("apellidos", apellidos);
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putString("fecha_nacimiento", fecha);
        bundle.putString("grado", grado);
        bundle.putString("curso", curso);

        intereses fragmentIntereses = new intereses();
        fragmentIntereses.setArguments(bundle);

        androidx.fragment.app.FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentIntereses);
        transaction.addToBackStack(null);
        transaction.commit();
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