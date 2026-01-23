package com.example.maps.model;


public class Country {

    private String nombre;
    private String iso2;
    private String bandera;

    public Country(String nombre, String iso2, String bandera) {
        this.nombre = nombre;
        this.iso2 = iso2;
        this.bandera = bandera;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIso2() {
        return iso2;
    }

    public String getBandera() {
        return bandera;
    }
}