package com.example.proyectofloppy;

public class Comunidad {

    private String id;
    private String nombre;
    private String ubicacion;
    private String adminId;
    private String descripcion;
    private String imagenUrl;
    private String ultimoMensaje;

    // Firebase SIEMPRE necesita un constructor vacío
    public Comunidad() {
    }

    public Comunidad(String id, String nombre, String ubicacion, String adminId, String descripcion, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.adminId = adminId;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getUltimoMensaje() { return ultimoMensaje; }
    public void setUltimoMensaje(String ultimoMensaje) { this.ultimoMensaje = ultimoMensaje; }
}
