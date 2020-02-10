package com.fcs.fcspos.model;

public class Sale {

    private String kind;
    private int product;
    private Vehicle vehicle;
    private int presetKind;
    private int money;
    private double voleme;


    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setPresetKind(int presetKind) {
        this.presetKind = presetKind;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setVoleme(double voleme) {
        this.voleme = voleme;
    }
}
