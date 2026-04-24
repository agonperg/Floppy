package com.example.proyectofloppy;

public class Viaje {
    private String id;
    private String origen;
    private String destino;
    private String fecha;
    private String hora;
    private double precio;
    private int plazas;
    private String conductorId;
    private String conductorNombre;

    // Firebase SIEMPRE necesita un constructor vacío
    public Viaje() {}

    public Viaje(String origen, String destino, String fecha, String hora,
                 double precio, int plazas, String conductorId, String conductorNombre) {
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.plazas = plazas;
        this.conductorId = conductorId;
        this.conductorNombre = conductorNombre;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getPlazas() { return plazas; }
    public void setPlazas(int plazas) { this.plazas = plazas; }

    public String getConductorId() { return conductorId; }
    public void setConductorId(String conductorId) { this.conductorId = conductorId; }

    public String getConductorNombre() { return conductorNombre; }
    public void setConductorNombre(String conductorNombre) { this.conductorNombre = conductorNombre; }
}
