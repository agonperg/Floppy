package com.example.proyectofloppy;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_foro_comunidad extends Fragment {

    private String comunidadId, nombreComunidad, imagenUrl, adminId, descripcion;
    private RecyclerView rvMensajes;
    private MensajeAdapter adapter;
    private List<Mensaje> listaMensajes;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText etMensaje;
    private ImageButton btnEnviar, btnVolver;
    private LinearLayout layoutInput;

    public static fragment_foro_comunidad newInstance(String id, String nombre, String imagen, String admin, String desc) {
        fragment_foro_comunidad fragment = new fragment_foro_comunidad();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("nombre", nombre);
        args.putString("imagen", imagen);
        args.putString("admin", admin);
        args.putString("descripcion", desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comunidadId = getArguments().getString("id");
            nombreComunidad = getArguments().getString("nombre");
            imagenUrl = getArguments().getString("imagen");
            adminId = getArguments().getString("admin");
            descripcion = getArguments().getString("descripcion");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foro_comunidad, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // UI Header
        btnVolver = view.findViewById(R.id.btnVolverForo);
        TextView tvNombre = view.findViewById(R.id.tvNombreForo);
        ImageView ivFoto = view.findViewById(R.id.ivFotoForo);
        
        tvNombre.setText(nombreComunidad);
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            Glide.with(this).load(imagenUrl).into(ivFoto);
        }

        // Click en el nombre para ver la descripción
        View header = view.findViewById(R.id.layoutHeaderForo); // Asumo que existe o uso el texto
        if (header == null) header = tvNombre;
        
        header.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle(nombreComunidad)
                    .setMessage(descripcion != null ? descripcion : "Sin descripción disponible.")
                    .setPositiveButton("Cerrar", null)
                    .show();
        });

        btnVolver.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // UI Chat
        rvMensajes = view.findViewById(R.id.rvMensajes);
        etMensaje = view.findViewById(R.id.etMensaje);
        btnEnviar = view.findViewById(R.id.btnEnviar);
        layoutInput = view.findViewById(R.id.layoutInput);

        listaMensajes = new ArrayList<>();
        adapter = new MensajeAdapter(listaMensajes);
        rvMensajes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMensajes.setAdapter(adapter);

        checkAdminStatus();
        btnEnviar.setOnClickListener(v -> enviarMensaje());
        escucharMensajes();

        return view;
    }

    private void checkAdminStatus() {
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "null";
        if (!currentUserId.equals(adminId)) {
            layoutInput.setVisibility(View.GONE);
        } else {
            layoutInput.setVisibility(View.VISIBLE);
        }
    }

    private void escucharMensajes() {
        db.collection("Comunidades").document(comunidadId)
                .collection("mensajes")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Mensaje m = dc.getDocument().toObject(Mensaje.class);
                                if (m != null) {
                                    m.setId(dc.getDocument().getId());
                                    listaMensajes.add(m);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (!listaMensajes.isEmpty()) {
                            rvMensajes.scrollToPosition(listaMensajes.size() - 1);
                        }
                    }
                });
    }

    private void enviarMensaje() {
        String texto = etMensaje.getText().toString().trim();
        if (texto.isEmpty()) return;

        String currentUserId = mAuth.getCurrentUser().getUid();
        String currentUserName = "Admin"; 

        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("contenido", texto);
        mensaje.put("emisorId", currentUserId);
        mensaje.put("emisorNombre", currentUserName);
        mensaje.put("timestamp", FieldValue.serverTimestamp());

        db.collection("Comunidades").document(comunidadId)
                .collection("mensajes").add(mensaje)
                .addOnSuccessListener(documentReference -> {
                    etMensaje.setText("");
                    // Actualizar el último mensaje en la comunidad para que se vea en la lista
                    db.collection("Comunidades").document(comunidadId)
                            .update("ultimoMensaje", texto);
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al enviar", Toast.LENGTH_SHORT).show());
    }
}
