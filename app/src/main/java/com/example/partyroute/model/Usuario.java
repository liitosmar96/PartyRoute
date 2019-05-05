package com.example.partyroute.model;

public class Usuario {
    private String NIF;
    private String nombre;
    private String correo;

    public Usuario() {
        this.NIF = "";
        this.nombre = "";
        this.correo = "";
    }

    public Usuario(String NIF, String nombre, String correo) {
        this();
        this.NIF = NIF;
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public boolean equals(Object object) {
        Usuario usuario = (Usuario) object;

        if (usuario.getCorreo().equals(this.correo) && usuario.getNIF().equals(this.NIF) && usuario.getNombre().equals(this.nombre)) {
            return true;
        }
        return false;
    }
}
