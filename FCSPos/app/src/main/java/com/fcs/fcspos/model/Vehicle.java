package com.fcs.fcspos.model;


import java.io.Serializable;

public class Vehicle implements Serializable {

    private String license_plate;
    private String kilometres;
    private int kind;//moto, taxi, pesado, liviano


    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getKilometres() {
        return kilometres;
    }

    public void setKilometres(String kilometres) {
        this.kilometres = kilometres;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "VEHICULO:Kind:" + kind + ";LicensePlate;" + license_plate + ";Kilometres;" + kilometres + ";";
    }
}
