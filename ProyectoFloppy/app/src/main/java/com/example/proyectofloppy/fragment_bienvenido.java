package com.example.proyectofloppy;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class fragment_bienvenido extends Fragment {

    private TextView tvUserName, tvNewCommunityName, tvNewTripInfo, tvVibeTitle, tvVibeMessage;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ListenerRegistration historyListener, newsListener;

    public fragment_bienvenido() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bienvenido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Inicializar vistas
        tvUserName = view.findViewById(R.id.tvUserName);
        tvNewCommunityName = view.findViewById(R.id.tvNewCommunityName);
        tvNewTripInfo = view.findViewById(R.id.tvNewTripInfo);
        tvVibeTitle = view.findViewById(R.id.tvVibeTitle);
        tvVibeMessage = view.findViewById(R.id.tvVibeMessage);
        ImageView fotoPerfil = view.findViewById(R.id.imageUserInfo);

        // Mostrar barra de navegación
        if (getActivity() instanceof activity_general) {
            ((activity_general) getActivity()).setBottomNavigationVisibility(View.VISIBLE);
        }

        if (fotoPerfil != null) {
            fotoPerfil.setOnClickListener(v -> irAAjustes());
        }

        // Cargar datos dinámicos
        cargarDatosUsuario();
        cargarNovedades();
        cargarNoticias(view);
        cargarHistorial(view);
        mostrarMensajeAleatorio();

        if (view.findViewById(R.id.cardNewCommunity) != null) {
            view.findViewById(R.id.cardNewCommunity).setOnClickListener(v -> {
                if (getActivity() instanceof activity_general) {
                    ((activity_general) getActivity()).getBottomNav().setSelectedItemId(R.id.nav_comunidades);
                }
            });
        }

        if (view.findViewById(R.id.cardNewTrip) != null) {
            view.findViewById(R.id.cardNewTrip).setOnClickListener(v -> {
                if (getActivity() instanceof activity_general) {
                    ((activity_general) getActivity()).getBottomNav().setSelectedItemId(R.id.nav_transporte);
                }
            });
        }
    }

    private void cargarDatosUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("nombre");
                            if (nombre != null && !nombre.isEmpty()) {
                                String formatted = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
                                tvUserName.setText(formatted + "! 👋");
                            }
                        }
                    });
        }
    }

    private void cargarNoticias(View view) {
        LinearLayout layoutNews = view.findViewById(R.id.layoutNews);
        if (layoutNews == null) return;

        // Intentar cargar noticias AUTOMÁTICAMENTE desde el Twitter de la UJA (via RSS)
        UjaTwitterService.fetchLatestTweets(tweets -> {
            if (!isAdded() || getContext() == null) return;
            
            layoutNews.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (tweets.isEmpty()) {
                // Si falla el servicio automático, mostrar las de ejemplo como fallback
                agregarNoticiaEjemplo(layoutNews, inflater, "Abierto el plazo de matrícula para los cursos de verano 2026. ¡No te quedes sin tu plaza! 🎓");
                agregarNoticiaEjemplo(layoutNews, inflater, "Mañana comienza la feria del libro en el edificio Zabaleta. ¡Pásate a vernos! 📚✨");
                agregarNoticiaEjemplo(layoutNews, inflater, "Nueva convocatoria de becas de movilidad internacional disponible. Consulta las bases. ✈️");
            } else {
                for (UjaTwitterService.Tweet tweet : tweets) {
                    agregarNoticiaTweet(layoutNews, inflater, tweet);
                }
            }
        });
    }

    private void agregarNoticiaTweet(LinearLayout container, LayoutInflater inflater, UjaTwitterService.Tweet tweet) {
        View card = inflater.inflate(R.layout.item_noticia, container, false);
        TextView tvContent = card.findViewById(R.id.tvNewsContent);
        TextView tvTime = card.findViewById(R.id.tvNewsTime);
        
        tvContent.setText(tweet.content);
        // Simplificamos la fecha para que se vea bien
        String fecha = tweet.date.length() > 16 ? tweet.date.substring(0, 16) : tweet.date;
        tvTime.setText(fecha);

        card.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(tweet.link));
            startActivity(intent);
        });

        container.addView(card);
    }

    private void agregarNoticiaEjemplo(LinearLayout container, LayoutInflater inflater, String texto) {
        View card = inflater.inflate(R.layout.item_noticia, container, false);
        TextView tvContent = card.findViewById(R.id.tvNewsContent);
        TextView tvTime = card.findViewById(R.id.tvNewsTime);
        
        tvContent.setText(texto);
        tvTime.setText("Noticia de ejemplo");

        card.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("https://x.com/ujaen?s=11"));
            startActivity(intent);
        });

        container.addView(card);
    }

    private void cargarNovedades() {
        // Última comunidad
        db.collection("Comunidades")
                .orderBy("nombre", Query.Direction.DESCENDING) // Fallback si no hay timestamp
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String nombre = querySnapshot.getDocuments().get(0).getString("nombre");
                        tvNewCommunityName.setText(nombre);
                    }
                });

        // Último viaje
        db.collection("viajes")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        Viaje v = querySnapshot.getDocuments().get(0).toObject(Viaje.class);
                        if (v != null) {
                            tvNewTripInfo.setText(v.getOrigen() + " ➔ " + v.getDestino());
                        }
                    }
                });
    }

    private void mostrarMensajeAleatorio() {
        String[] titulos = {"Tip del día 💡", "¡Vamos Floppy! 🚀", "¿Sabías qué? 🤔", "Comunidad activa ✨"};
        String[] mensajes = {
            "¿Sabías que puedes compartir tus apuntes y ganar puntos de reputación?",
            "Revisa la sección de transporte, ¡seguro que alguien va hacia tu destino!",
            "Únete a la comunidad de tu grado para no perderte ningún apunte importante.",
            "Recuerda que puedes personalizar tu perfil desde los ajustes.",
            "¡Hoy es un gran día para aprender algo nuevo en el edificio Zabaleta!"
        };

        Random r = new Random();
        tvVibeTitle.setText(titulos[r.nextInt(titulos.length)]);
        tvVibeMessage.setText(mensajes[r.nextInt(mensajes.length)]);
    }

    private void cargarHistorial(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        LinearLayout layoutHistory = view.findViewById(R.id.layoutHistory);
        View tvHistoryTitle = view.findViewById(R.id.tvHistoryTitle);
        View hsvHistory = view.findViewById(R.id.hsvHistory);

        // Quitamos getActivity() y lo gestionamos manualmente para evitar errores de FragmentManager
        historyListener = db.collection("users").document(user.getUid())
                .addSnapshotListener((doc, error) -> {
                    if (error != null) return;
                    if (doc != null && doc.exists() && doc.get("historial") != null) {
                        List<Map<String, Object>> historialRaw = new ArrayList<>((List<Map<String, Object>>) doc.get("historial"));
                        if (!historialRaw.isEmpty()) {
                            Collections.reverse(historialRaw);
                            List<Map<String, Object>> historialFiltrado = new ArrayList<>();
                            Set<String> idsVisitados = new HashSet<>();
                            
                            for (Map<String, Object> item : historialRaw) {
                                String id = (String) item.get("id");
                                if (id != null && !idsVisitados.contains(id)) {
                                    historialFiltrado.add(item);
                                    idsVisitados.add(id);
                                }
                                if (historialFiltrado.size() >= 5) break;
                            }

                            if (!historialFiltrado.isEmpty()) {
                                tvHistoryTitle.setVisibility(View.VISIBLE);
                                hsvHistory.setVisibility(View.VISIBLE);
                                layoutHistory.removeAllViews();

                                LayoutInflater inflater = LayoutInflater.from(getContext());
                                for (Map<String, Object> item : historialFiltrado) {
                                    agregarItemHistorial(layoutHistory, inflater, item);
                                }
                            }
                        }
                    }
                });
    }

    private void agregarItemHistorial(LinearLayout container, LayoutInflater inflater, Map<String, Object> data) {
        View card = inflater.inflate(R.layout.item_historial, container, false);
        
        TextView tvName = card.findViewById(R.id.tvHistoryName);
        TextView tvType = card.findViewById(R.id.tvHistoryType);
        ImageView ivIcon = card.findViewById(R.id.ivHistoryIcon);

        String tipo = (String) data.get("tipo");
        String nombre = (String) data.get("nombre");
        String id = (String) data.get("id");

        tvName.setText(nombre);
        tvType.setText(tipo.substring(0, 1).toUpperCase() + tipo.substring(1));

        if (tipo.equals("comunidad")) {
            ivIcon.setImageResource(R.drawable.groups);
        } else if (tipo.equals("viaje")) {
            ivIcon.setImageResource(android.R.drawable.ic_menu_directions);
        }

        card.setOnClickListener(v -> {
            if (tipo.equals("comunidad")) {
                abrirComunidad(id, nombre);
            } else if (tipo.equals("viaje")) {
                if (getActivity() instanceof activity_general) {
                    ((activity_general) getActivity()).getBottomNav().setSelectedItemId(R.id.nav_transporte);
                }
            }
        });

        container.addView(card);
    }

    private void abrirComunidad(String id, String nombre) {
        fragment_foro_comunidad fragment = new fragment_foro_comunidad();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("nombre", nombre);
        fragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (historyListener != null) {
            historyListener.remove();
        }
        if (newsListener != null) {
            newsListener.remove();
        }
    }

    private void irAAjustes() {
        fragment_ajustesCuenta fragmentAjustes = new fragment_ajustesCuenta();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentAjustes);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}