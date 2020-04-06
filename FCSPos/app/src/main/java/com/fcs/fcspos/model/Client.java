package com.fcs.fcspos.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Client implements Serializable {
    private String identificationCard;
    private String nit;
    private byte authorizedProduct;
    private short authorizedPpu;
    private String message;
    private String name;
    private int availableMoney;
    private double availableVolume;
    private int availableFull;


    public int getAvailableFull() {
        return availableFull;
    }

    public void setAvailableFull(int availableFull) {
        this.availableFull = availableFull;
    }

    public int getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(int availableMoney) {
        this.availableMoney = availableMoney;
    }

    public double getAvailableVolume() {
        return availableVolume;
    }

    public void setAvailableVolume(double availableVolume) {
        this.availableVolume = availableVolume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getAuthorizedProduct() {
        return authorizedProduct;
    }

    public void setAuthorizedProduct(byte authorizedProduct) {
        this.authorizedProduct = authorizedProduct;
    }

    public short getAuthorizedPpu() {
        return authorizedPpu;
    }

    public void setAuthorizedPpu(short authorizedPpu) {
        this.authorizedPpu = authorizedPpu;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdentificationCard() {
        return identificationCard;
    }

    public void setIdentificationCard(String identificationCard) {
        this.identificationCard = identificationCard;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    @NonNull
    @Override
    public String toString() {
        return "Client:IdentificationCard;" + identificationCard + ";nit;" + nit +
                ";AuthorizedProduct;" +authorizedProduct + ";AuthorizedPpu;" + authorizedPpu
                + ";Message;" + message + ";Name" + name + ";AvailableMoney" + availableMoney
                + ";AvailableVolume;" + availableVolume + ";";
    }
}
