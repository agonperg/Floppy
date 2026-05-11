package com.example.proyectofloppy;

import com.google.firebase.Timestamp;

public class Mensaje {
    private String id;
    private String contenido;
    private String emisorId;
    private String emisorNombre;
    private Timestamp timestamp;

    public Mensaje() {
    }

    public Mensaje(String id, String contenido, String emisorId, String emisorNombre, Timestamp timestamp) {
        this.id = id;
        this.contenido = contenido;
        this.emisorId = emisorId;
        this.emisorNombre = emisorNombre;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getEmisorId() { return emisorId; }
    public void setEmisorId(String emisorId) { this.emisorId = emisorId; }

    public String getEmisorNombre() { return emisorNombre; }
    public void setEmisorNombre(String emisorNombre) { this.emisorNombre = emisorNombre; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
