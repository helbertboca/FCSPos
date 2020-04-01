package com.fcs.fcspos.model;

import androidx.annotation.NonNull;

public class Sale {

    private short position;
    private short ok;
    private short hose;
    private double volume;
    private int money;
    private int ppu;
    private Client client;
    private Vehicle vehicle;


    public Sale(short position, short ok, short hose, double volume, int money, int ppu) {
        this.position = position;
        this.ok = ok;
        this.hose = hose;
        this.volume = volume;
        this.money = money;
        this.ppu = ppu;
    }

    public short getPosition() {
        return position;
    }

    public short getManguera() {
        return hose;
    }

    public double getVolumen() {
        return volume;
    }

    public int getDinero() {
        return money;
    }

    public int getPpu() {
        return ppu;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @NonNull
    @Override
    public String toString() {
        return  "Sale:Position;" + position +";Hose;" + hose +";Volume;" + volume +";Money;" + money +";Ppu;" + ppu + ";";
    }
}
