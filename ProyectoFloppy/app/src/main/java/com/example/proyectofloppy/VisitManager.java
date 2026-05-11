package com.example.proyectofloppy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class VisitManager {

    public static void registrarVisita(String tipo, String id, String nombre) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> visita = new HashMap<>();
        visita.put("tipo", tipo); // "comunidad", "viaje", etc.
        visita.put("id", id);
        visita.put("nombre", nombre);
        visita.put("timestamp", System.currentTimeMillis());

        // Eliminamos si ya existe para evitar duplicados en el historial reciente
        // (Firestore no permite eliminar por ID fácilmente en un array sin conocer el objeto exacto,
        // así que primero lo buscamos o simplemente lo añadimos al principio y limitamos en la UI)
        
        // Estrategia: Añadir al inicio y luego el fragment_bienvenido mostrará los únicos más recientes.
        db.collection("users").document(user.getUid())
                .update("historial", FieldValue.arrayUnion(visita));
    }
}
