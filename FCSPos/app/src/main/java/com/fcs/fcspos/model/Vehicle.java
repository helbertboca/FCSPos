package com.fcs.fcspos.model;


public class Vehicle {

    private String class_veh; //moto, taxi, pesado, liviano
    private String license_plate;
    private String kilometres;
    private int kind;

    public void setKind(int kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "VEHICULO. tipo:" + kind;
    }
}
