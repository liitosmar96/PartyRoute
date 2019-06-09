package com.example.partyroute.model;

import java.io.Serializable;

public class Evento implements Serializable {
    private int ID;
    private String fecha;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String edad;
    private String imagen;

    public Evento() {
        this.ID = 0;
        this.fecha = "";
        this.nombre = "";
        this.descripcion = "";
        this.edad = "+0";
        this.imagen = "";
    }

    public Evento(String fecha, String nombre, String descripcion, String direccion, String edad, String imagen) {
        this();
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.nombre = nombre;
        this.edad = edad;
        this.imagen = imagen;
    }

    public Evento(int ID, String fecha, String nombre, String descripcion, String direccion, String edad, String imagen) {
        this(fecha, nombre,descripcion,direccion,edad,imagen);
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
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

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String toString() {
        return "Evento[ID = " + ID + ", FECHA = " + fecha + ", NOMBRE = " + nombre + ", DESCRIPCION = " + descripcion + ", DIRECCION = " + getDireccion() +
                ", EDAD = " + edad + ", IMAGEN = " + imagen + "]";
    }

}
