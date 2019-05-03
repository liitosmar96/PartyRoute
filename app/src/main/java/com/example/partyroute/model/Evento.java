package com.example.partyroute.model;

import java.sql.Date;

public class Evento {
    private int ID;
    private Date fecha;
    private String nombre;
    private String descripcion;
    private int latitud;
    private int longitud;
    private String edad;
    private int rate;
    private byte[] imagen;

    public Evento() {
        this.ID = 0;
        java.util.Date utilDate = new java.util.Date();
        this.fecha = new java.sql.Date(utilDate.getTime());
        this.nombre = "Nombre";
        this.descripcion = "Descripcionaca besrtial sobre el evento que se celebra en mi culo";
        this.latitud = 0;
        this.longitud = 0;
        this.edad = "+" + ((int) (Math.random() * 100) + 1);
        this.rate = (int) (Math.random() * 10) + 1;
        this.imagen = new byte[0];
    }

    public Evento(int ID, Date fecha, String nombre, String descripcion, int latitud, int longitud, String edad, int rate, byte[] imagen) {
        this();
        this.ID = ID;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.edad = edad;
        this.rate = rate;
        this.imagen = imagen;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getLatitud() {
        return latitud;
    }

    public void setLatitud(int latitud) {
        this.latitud = latitud;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
