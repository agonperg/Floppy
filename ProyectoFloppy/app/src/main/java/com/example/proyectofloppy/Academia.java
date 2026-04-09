package com.example.proyectofloppy;

import com.google.firebase.Timestamp;

public class Academia {
    private String id;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String creadorId;
    private Timestamp timestamp;

    public Academia() {
        // Constructor vacío requerido por Firestore
    }

    public Academia(String id, String nombre, String descripcion, String direccion, String creadorId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.creadorId = creadorId;
        this.timestamp = Timestamp.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCreadorId() { return creadorId; }
    public void setCreadorId(String creadorId) { this.creadorId = creadorId; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
