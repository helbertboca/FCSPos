package com.fcs.fcspos.model;

import java.io.Serializable;

public class Station implements Serializable {

    private String nombre;
    private String nit;
    private String telefono;
    private String direccion;
    private String ciudad;

    public Station(String nombre, String nit, String telefono, String direccion, String ciudad) {
        this.nombre = nombre;
        this.nit = nit;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNit() {
        return nit;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }
}
