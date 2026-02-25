package com.example.proyectofloppy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class crear_cuenta_docente extends Fragment {

    private EditText etAcademyName, etPassword, etPhone, etAddress, etWebsite;
    private TextView tvSelectorSpecialization, tvLoginLink;
    private View btnCreateAccount;

    public crear_cuenta_docente() {
    }

    public static crear_cuenta_docente newInstance(String param1, String param2) {
        crear_cuenta_docente fragment = new crear_cuenta_docente();
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
        return inflater.inflate(R.layout.fragment_crear_cuenta_docente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etAcademyName = view.findViewById(R.id.etAcademyName);
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
                validarYPasarAIntereses();
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

    private void validarYPasarAIntereses() {
        String nombre = etAcademyName.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String telf = etPhone.getText().toString().trim();
        String direccion = etAddress.getText().toString().trim();
        String especialidad = tvSelectorSpecialization.getText().toString().trim();
        String web = etWebsite.getText().toString().trim();

        if (nombre.isEmpty() || pass.isEmpty() || telf.isEmpty() || direccion.isEmpty() || especialidad.isEmpty() || especialidad.equals("Área/as de especialización")) {
            Snackbar.make(requireView(), "Por favor, rellena todos los campos obligatorios", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Snackbar.make(requireView(), "La contraseña debe tener al menos 6 caracteres", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("nombre", nombre);
        bundle.putString("password", pass);
        bundle.putString("telefono", telf);
        bundle.putString("direccion", direccion);
        bundle.putString("especializacion", especialidad);
        bundle.putString("website", web);
        bundle.putBoolean("esDocente", true);

        intereses fragmentIntereses = new intereses();
        fragmentIntereses.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentIntereses);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}