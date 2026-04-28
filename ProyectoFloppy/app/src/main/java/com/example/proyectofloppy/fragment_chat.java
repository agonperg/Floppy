package com.example.proyectofloppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_chat extends Fragment {

    private String comunidadId, creadorId;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private MensajeAdapter adapter;
    private List<Mensaje> listaMensajes;
    private LinearLayout layoutEscribir;
    private EditText etMensaje;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Recuperar los datos enviados desde fragment_comunidades
        if (getArguments() != null) {
            comunidadId = getArguments().getString("comunidadId");
            creadorId = getArguments().getString("creadorId");
            String nombre = getArguments().getString("nombreComunidad");
            ((TextView)view.findViewById(R.id.tvNombreChat)).setText(nombre);
        }

        layoutEscribir = view.findViewById(R.id.layoutEscribir);
        etMensaje = view.findViewById(R.id.etMensajeChat);
        RecyclerView rv = view.findViewById(R.id.rvMensajes);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        listaMensajes = new ArrayList<>();
        adapter = new MensajeAdapter(listaMensajes);
        rv.setAdapter(adapter);

        // --- LÓGICA DE ADMINISTRADOR ---
        // Obtenemos nuestro propio ID
        String miUid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "admin_test";

        // Si mi ID NO coincide con el del creador de la comunidad, escondo la barra de escritura
        if (miUid.equals(creadorId)) {
            layoutEscribir.setVisibility(View.VISIBLE);
        } else {
            layoutEscribir.setVisibility(View.GONE);
        }

        // Botón enviar mensaje
        view.findViewById(R.id.btnEnviarMensaje).setOnClickListener(v -> {
            enviarMensaje(etMensaje.getText().toString());
        });

        // Empezar a escuchar mensajes en tiempo real
        escucharMensajes();
        return view;
    }

    private void enviarMensaje(String texto) {
        if (texto.isEmpty()) return;

        Map<String, Object> msg = new HashMap<>();
        msg.put("texto", texto);
        msg.put("nombreAdmin", "Admin de la Comunidad");
        msg.put("timestamp", System.currentTimeMillis());

        // Guardamos el mensaje en una subcolección "Mensajes" dentro de la comunidad específica
        db.collection("Comunidades").document(comunidadId)
                .collection("Mensajes").add(msg)
                .addOnSuccessListener(doc -> etMensaje.setText(""));
    }

    private void escucharMensajes() {
        // addSnapshotListener mantiene la conexión abierta para recibir mensajes nuevos al instante
        db.collection("Comunidades").document(comunidadId).collection("Mensajes")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, e) -> {
                    if (value == null) return;

                    listaMensajes.clear();
                    for (DocumentSnapshot doc : value) {
                        // Convertimos el documento de Firestore a nuestro objeto Mensaje
                        Mensaje m = doc.toObject(Mensaje.class);
                        listaMensajes.add(m);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}