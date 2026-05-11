package com.example.proyectofloppy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class fragment_ajustesCuenta extends Fragment {

    private EditText etNombre, etApellidos, etFecha;
    private TextView tvGrado, tvCurso;
    private ImageView ivFotoPerfil, ivSettings;
    private Button btnGuardar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Uri imagenUri = null;
    private String urlImagenActual = null;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public fragment_ajustesCuenta() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                imagenUri = uri;
                ivFotoPerfil.setImageURI(uri);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajustes_cuenta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etNombre = view.findViewById(R.id.etNombre);
        etApellidos = view.findViewById(R.id.etApellidos);
        etFecha = view.findViewById(R.id.etFecha);
        tvGrado = view.findViewById(R.id.tvGrado);
        tvCurso = view.findViewById(R.id.tvCurso);
        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil);
        ivSettings = view.findViewById(R.id.ivSettings);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        cargarDatosUsuario();

        // Configurar selectores (mismo estilo que registro)
        configurarSelectorFecha();
        
        // Nuevos touch targets para grado y curso (más intuitivos)
        view.findViewById(R.id.btnSelectGrado).setOnClickListener(v -> abrirSelectorGrado());
        view.findViewById(R.id.btnSelectCurso).setOnClickListener(v -> abrirSelectorCurso());

        // Selector de foto
        View.OnClickListener openPicker = v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
        view.findViewById(R.id.btnCambiarFoto).setOnClickListener(openPicker);
        ivFotoPerfil.setOnClickListener(openPicker);

        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(v -> validarYGuardar());
        }

        if (ivSettings != null) {
            ivSettings.setOnClickListener(this::mostrarMenuDesplegable);
        }
    }

    private void cargarDatosUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        etNombre.setText(doc.getString("nombre"));
                        etApellidos.setText(doc.getString("apellidos"));
                        etFecha.setText(doc.getString("fecha_nacimiento"));
                        tvGrado.setText(doc.getString("grado"));
                        tvCurso.setText(doc.getString("curso"));
                        urlImagenActual = doc.getString("fotoUrl"); // Supongamos que se llama así

                        if (urlImagenActual != null && !urlImagenActual.isEmpty()) {
                            Glide.with(this).load(urlImagenActual).into(ivFotoPerfil);
                        }
                    }
                });
    }

    private void validarYGuardar() {
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("Obligatorio");
            return;
        }

        // Formatear: Primera en mayúscula, resto en minúscula
        String nombreFinal = formatName(nombre);
        String apellidosFinal = formatName(apellidos);

        if (imagenUri != null) {
            subirImagenYGuardar(nombreFinal, apellidosFinal);
        } else {
            guardarDatosFinal(nombreFinal, apellidosFinal, urlImagenActual);
        }
    }

    private void subirImagenYGuardar(String nombre, String apellidos) {
        Toast.makeText(getContext(), "Actualizando foto...", Toast.LENGTH_SHORT).show();
        MediaManager.get().upload(imagenUri)
                .option("resource_type", "image")
                .unsigned("preset_floppy")
                .callback(new UploadCallback() {
                    @Override public void onStart(String requestId) {}
                    @Override public void onProgress(String requestId, long bytes, long totalBytes) {}
                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String url = (String) resultData.get("secure_url");
                        guardarDatosFinal(nombre, apellidos, url);
                    }
                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(getContext(), "Error subiendo imagen", Toast.LENGTH_SHORT).show();
                        guardarDatosFinal(nombre, apellidos, urlImagenActual);
                    }
                    @Override public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void guardarDatosFinal(String nombre, String apellidos, String url) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", nombre);
        updates.put("apellidos", apellidos);
        updates.put("fecha_nacimiento", etFecha.getText().toString());
        updates.put("grado", tvGrado.getText().toString());
        updates.put("curso", tvCurso.getText().toString());
        updates.put("fotoUrl", url);

        db.collection("users").document(user.getUid()).update(updates)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    volverABienvenida();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show());
    }

    private void configurarSelectorFecha() {
        etFecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            new DatePickerDialog(getContext(), (view, year, month, day) -> {
                etFecha.setText(day + "/" + (month + 1) + "/" + year);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void abrirSelectorGrado() {
        final String[] opciones = {"Ingeniería Informática", "Medicina", "Derecho", "ADE", "Arquitectura", "Psicología", "Enfermería", "Economía"};
        new AlertDialog.Builder(getContext()).setTitle("Selecciona tu grado")
                .setItems(opciones, (dialog, i) -> tvGrado.setText(opciones[i])).show();
    }

    private void abrirSelectorCurso() {
        final String[] opciones = {"1º", "2º", "3º", "4º"};
        new AlertDialog.Builder(getContext()).setTitle("Selecciona tu curso")
                .setItems(opciones, (dialog, i) -> tvCurso.setText(opciones[i])).show();
    }

    private String formatName(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void mostrarMenuDesplegable(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenu().add(0, 1, 0, "Cerrar sesión");
        popup.getMenu().add(0, 2, 1, "Tema (Oscuro/Claro)");

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), activity_login.class));
                getActivity().finish();
                return true;
            } else if (item.getItemId() == 2) {
                alternarModoOscuro();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void alternarModoOscuro() {
        int modoActual = AppCompatDelegate.getDefaultNightMode();
        if (modoActual == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void volverABienvenida() {
        if (getParentFragmentManager() != null) {
            getParentFragmentManager().popBackStack();
        }
    }
}