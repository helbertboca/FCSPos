package com.fcs.fcspos.model;

import android.support.annotation.NonNull;

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

    public String getKind() {
        return kind;
    }

    public int getProduct() {
        return product;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getPresetKind() {
        return presetKind;
    }

    public int getMoney() {
        return money;
    }

    public double getVoleme() {
        return voleme;
    }

    @NonNull
    @Override
    public String toString() {
        return "VENTA.  Tipo:" + kind + ", Producto:" + product + ", Vehiculo:" + vehicle + ", Tipo de Preset:" + presetKind + ", Dinero:" + money + ", Volumen:"+ voleme ;
    }
}
