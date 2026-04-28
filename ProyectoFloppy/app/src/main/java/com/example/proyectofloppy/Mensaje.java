package com.example.proyectofloppy;

public class Mensaje {
    private String texto;
    private String nombreAdmin;
    private long timestamp;

    public Mensaje() {}

    public Mensaje(String texto, String nombreAdmin, long timestamp) {
        this.texto = texto;
        this.nombreAdmin = nombreAdmin;
        this.timestamp = timestamp;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getNombreAdmin() { return nombreAdmin; }
    public void setNombreAdmin(String nombreAdmin) { this.nombreAdmin = nombreAdmin; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
