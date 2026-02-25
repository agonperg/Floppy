package com.example.proyectofloppy;

public class Comunidad {

    private String id;
    private String nombre;
    private String ubicacion;

    // Firebase SIEMPRE necesita un constructor vacío
    public Comunidad() {
    }

    public Comunidad(String id, String nombre, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}
