package com.fcs.fcspos.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Programming implements Serializable {

    private String kind;
    private int product;
    private Vehicle vehicle;
    private int presetKind;
    private int quantity;
    private byte position;


    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getProduct() {
        return product;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setPresetKind(int presetKind) {
        this.presetKind = presetKind;
    }

    public int getPresetKind() {
        return presetKind;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() { return quantity; }

    public void setPosition(byte position) {
        this.position = position;
    }

    public byte getPosition() {
        return position;
    }

    @NonNull
    @Override
    public String toString() {
        return "VENTA.  Tipo:" + kind + ", Producto:" + product + ", Vehiculo:" + vehicle + ", Tipo de Preset:" + presetKind + ", Cantidad:" + quantity ;
    }
}
