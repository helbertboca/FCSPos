package com.fcs.fcspos.model;


public class Vehicle {

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
        return "VEHICULO. tipo:" + kind + ", Placa:" + license_plate + ", kilometraje:" + kilometres;
    }
}
