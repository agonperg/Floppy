package com.example.proyectofloppy;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class fragment_crear_comunidad extends Fragment {

    private TextInputEditText etNombre, etDescripcion;
    private MaterialButton btnCrearFinal;
    private ImageView ivPerfil;
    private TextView tvAnadirFoto;
    private ImageButton btnVolver;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Uri imagenUri = null;
    
    // Usamos el NUEVO Photo Picker de Android (Más visual y moderno)
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                imagenUri = uri;
                ivPerfil.setImageURI(uri);
                Log.d("PhotoPicker", "Selected URI: " + uri);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_comunidad, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etNombre = view.findViewById(R.id.etNombreComunidad);
        etDescripcion = view.findViewById(R.id.etDescripcionComunidad);
        btnCrearFinal = view.findViewById(R.id.btnCrearFinal);
        ivPerfil = view.findViewById(R.id.imageViewPerfil);
        tvAnadirFoto = view.findViewById(R.id.tvAnadirFoto);
        btnVolver = view.findViewById(R.id.btnVolverCrear);

        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        View.OnClickListener openPicker = v -> {
            // Abrir el selector visual de fotos
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        };

        tvAnadirFoto.setOnClickListener(openPicker);
        if (view.findViewById(R.id.cardFotoPerfil) != null) {
            view.findViewById(R.id.cardFotoPerfil).setOnClickListener(openPicker);
        }
        ivPerfil.setOnClickListener(openPicker);

        btnCrearFinal.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            if (nombre.isEmpty()) {
                etNombre.setError(getString(R.string.comunidad_escribe_nombre_error));
                return;
            }

            if (imagenUri != null) {
                subirAFirebaseConImagen(nombre, descripcion, imagenUri);
            } else {
                guardarEnFirebase(nombre, descripcion, null);
            }
        });

        return view;
    }

    private void subirAFirebaseConImagen(String nombre, String descripcion, Uri uri) {
        Toast.makeText(getContext(), getString(R.string.comunidad_subiendo_imagen), Toast.LENGTH_SHORT).show();

        MediaManager.get().upload(uri)
                .option("resource_type", "image")
                .unsigned("preset_floppy")
                .callback(new UploadCallback() {
                    @Override public void onStart(String requestId) {}
                    @Override public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String urlImagen = (String) resultData.get("secure_url");
                        guardarEnFirebase(nombre, descripcion, urlImagen);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.e("Cloudinary", "Error: " + error.getDescription());
                        Toast.makeText(getContext(), getString(R.string.comunidad_error_subir_imagen), Toast.LENGTH_SHORT).show();
                        guardarEnFirebase(nombre, descripcion, null);
                    }

                    @Override public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void guardarEnFirebase(String nombre, String descripcion, String urlImagen) {
        com.google.firebase.auth.FirebaseUser user = mAuth.getCurrentUser();
        String adminId = user != null ? user.getUid() : "admin_test";
        
        Map<String, Object> nuevaComunidad = new HashMap<>();
        nuevaComunidad.put("nombre", nombre);
        nuevaComunidad.put("descripcion", descripcion.isEmpty() ? getString(R.string.comunidad_sin_descripcion) : descripcion);
        nuevaComunidad.put("adminId", adminId);
        nuevaComunidad.put("imagenUrl", urlImagen);
        nuevaComunidad.put("ubicacion", "Universidad de Jaén");

        db.collection("Comunidades").add(nuevaComunidad)
                .addOnSuccessListener(documentReference -> {
                    String comunidadId = documentReference.getId();
                    publicarMensajeBienvenida(comunidadId, nombre);
                    unirAdminAComunidad(adminId, comunidadId);

                    Toast.makeText(getContext(), getString(R.string.comunidad_creada_ok), Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void publicarMensajeBienvenida(String comunidadId, String nombreCarrera) {
        String mensajeTexto = "¡Bienvenidos al barco, futuros " + nombreCarrera + "! 🎓\uD83D\uDCAA\u200D♂️\n\n" +
                "Ya estamos oficialmente dentro de la UJA. Si habéis llegado hasta aquí es porque tenéis ganas de aprender (o porque os gusta mucho el café del Vial).\n\n" +
                "En este grupo no solo compartiremos apuntes y fechas de exámenes, también estamos para:\n\n" +
                "Darnos apoyo moral cuando el campus de Las Lagunillas se nos haga cuesta arriba.\n\n" +
                "Preguntar \"eso entra para el examen\" 50 veces.\n\n" +
                "Organizar las mejores quedadas post-parciales.\n\n" +
                "¡A darle caña, que el Grado no se va a sacar solo! 🦁✨";

        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("contenido", mensajeTexto);
        mensaje.put("emisorId", "system");
        mensaje.put("emisorNombre", "Floppy Bot");
        mensaje.put("timestamp", FieldValue.serverTimestamp());

        db.collection("Comunidades").document(comunidadId)
                .collection("mensajes").add(mensaje)
                .addOnSuccessListener(doc -> {
                    // También guardamos el mensaje en el documento de la comunidad para la lista
                    db.collection("Comunidades").document(comunidadId)
                            .update("ultimoMensaje", mensajeTexto);
                });
    }

    private void unirAdminAComunidad(String userId, String comunidadId) {
        db.collection("users").document(userId)
                .update("misComunidades", FieldValue.arrayUnion(comunidadId));
    }
}