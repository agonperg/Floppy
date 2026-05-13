package com.example.proyectofloppy;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_comunidades extends Fragment {

    private RecyclerView recyclerView;
    private ComunidadAdapter adapter;
    private List<Comunidad> listaComunidades = new ArrayList<>();
    private List<Comunidad> listaOriginal = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ListenerRegistration userListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comunidades, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ImageButton btnCrear = view.findViewById(R.id.btnCrearComunidad);
        ImageButton btnUnirme = view.findViewById(R.id.btnUnirme);

        // Truco para re-seed: Click largo en el título
        view.findViewById(R.id.tvTituloComunidades).setOnLongClickListener(v -> {
            seedTestData();
            return true;
        });

        EditText etBuscar = view.findViewById(R.id.etBuscadorComunidad);
        recyclerView = view.findViewById(R.id.rvComunidades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ComunidadAdapter(listaComunidades);
        adapter.setEsModoBusqueda(false); 
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(comunidad -> {
            navegarAFragment(fragment_foro_comunidad.newInstance(
                    comunidad.getId(),
                    comunidad.getNombre(),
                    comunidad.getImagenUrl(),
                    comunidad.getAdminId(),
                    comunidad.getDescripcion()
            ));
        });

        adapter.setOnAccionClickListener(this::abandonarComunidad);

        if (etBuscar != null) {
            etBuscar.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void afterTextChanged(Editable s) {
                    filtrarBuscador(s.toString());
                }
            });
        }

        btnCrear.setOnClickListener(v -> navegarAFragment(new fragment_crear_comunidad()));
        btnUnirme.setOnClickListener(v -> navegarAFragment(new fragment_unirse_comunidad()));

        iniciarEscuchaUsuario();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (userListener == null) {
            iniciarEscuchaUsuario();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (userListener != null) {
            userListener.remove();
            userListener = null;
        }
    }

    private void iniciarEscuchaUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        userListener = db.collection("users").document(user.getUid()).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) return;

            if (documentSnapshot != null && documentSnapshot.exists()) {
                List<String> ids = (List<String>) documentSnapshot.get("misComunidades");
                
                if (ids != null && !ids.isEmpty()) {
                    db.collection("Comunidades")
                            .whereIn(FieldPath.documentId(), ids)
                            .addSnapshotListener((querySnapshot, error) -> {
                                if (error != null) return;
                                if (querySnapshot != null) {
                                    List<Comunidad> nuevas = new ArrayList<>();
                                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                        Comunidad c = doc.toObject(Comunidad.class);
                                        if (c != null) {
                                            c.setId(doc.getId());
                                            // Fallback: Si el campo ultimoMensaje no está en el documento, intentamos traerlo manualmente
                                            if (c.getUltimoMensaje() == null) {
                                                fetchUltimoMensajeFallback(c);
                                            }
                                            nuevas.add(c);
                                        }
                                    }
                                    listaComunidades.clear();
                                    listaComunidades.addAll(nuevas);
                                    listaOriginal.clear();
                                    listaOriginal.addAll(nuevas);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                } else if (ids != null) {
                    listaComunidades.clear();
                    listaOriginal.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void fetchUltimoMensajeFallback(Comunidad c) {
        db.collection("Comunidades").document(c.getId())
                .collection("mensajes")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        String contenido = doc.getString("contenido");
                        if (contenido != null) {
                            c.setUltimoMensaje(contenido);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void filtrarBuscador(String textoBusqueda) {
        List<Comunidad> filtrada = new ArrayList<>();
        for (Comunidad c : listaOriginal) {
            if (c.getNombre() != null && c.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                filtrada.add(c);
            }
        }
        adapter.filtrar(filtrada);
    }

    private void seedTestData() {
        Toast.makeText(getContext(), getString(R.string.comunidad_limpiando_db), Toast.LENGTH_SHORT).show();
        db.collection("Comunidades").get().addOnSuccessListener(querySnapshot -> {
            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                doc.getReference().delete();
            }
            crearComunidadesReales();
        });
    }

    private void crearComunidadesReales() {
        String[] nombres = {"Grado en Ingeniería Informática", "Grado en Derecho", "Grado en Enfermería", "Grado en ADE"};
        String[] descripciones = {
                "Comunidad oficial para estudiantes de Informática en la UJA.",
                "Espacio para debatir leyes y compartir apuntes de Derecho.",
                "Apoyo y recursos para los futuros enfermeros y enfermeras.",
                "Gestión, economía y todo lo relacionado con ADE en Las Lagunillas."
        };

        for (int i = 0; i < nombres.length; i++) {
            final String carrera = nombres[i];
            final String desc = descripciones[i];

            Map<String, Object> c = new HashMap<>();
            c.put("nombre", carrera);
            c.put("descripcion", desc);
            c.put("ubicacion", "Universidad de Jaén");
            c.put("adminId", "system");
            c.put("imagenUrl", null);

            db.collection("Comunidades").add(c).addOnSuccessListener(doc -> {
                String mensajeTexto = "¡Bienvenidos al barco, futuros " + carrera + "! 🎓🚣‍♂️\n\n" +
                        "Ya estamos oficialmente dentro de la UJA. Si habéis llegado hasta aquí es porque tenéis ganas de aprender (o porque os gusta mucho el café del Vial).\n\n" +
                        "En este grupo no solo compartiremos apuntes y fechas de exámenes, también estamos para:\n\n" +
                        "Darnos apoyo moral cuando el campus de Las Lagunillas se nos haga cuesta arriba.\n\n" +
                        "Preguntar \"eso entra para el examen\" 50 veces.\n\n" +
                        "Organizar las mejores quedadas post-parciales.\n\n" +
                        "¡A darle caña, que el Grado no se va a sacar solo! 🦁✨";

                Map<String, Object> m = new HashMap<>();
                m.put("contenido", mensajeTexto);
                m.put("emisorId", "system");
                m.put("emisorNombre", "Floppy Bot");
                m.put("timestamp", FieldValue.serverTimestamp());
                doc.collection("mensajes").add(m).addOnSuccessListener(mensajeDoc -> {
                    // Actualizar el último mensaje en la comunidad para la lista
                    doc.update("ultimoMensaje", mensajeTexto);
                });
            });
        }
        Toast.makeText(getContext(), getString(R.string.comunidad_db_actualizada), Toast.LENGTH_SHORT).show();
    }

    private void abandonarComunidad(Comunidad comunidad) {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.comunidad_abandonar_confirm_titulo))
                .setMessage(getString(R.string.comunidad_abandonar_confirm_msg, comunidad.getNombre()))
                .setPositiveButton(getString(R.string.comunidad_salir_si), (dialog, which) -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        db.collection("users").document(user.getUid())
                                .update("misComunidades", FieldValue.arrayRemove(comunidad.getId()))
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), getString(R.string.comunidad_has_salido), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .setNegativeButton(getString(R.string.comunidad_cancelar), null)
                .show();
    }

    private void navegarAFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}