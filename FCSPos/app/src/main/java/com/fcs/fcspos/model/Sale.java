package com.fcs.fcspos.model;

import androidx.annotation.NonNull;

public class Sale {

    private short position;
    private short ok;
    private short manguera;
    private double volumen;
    private int dinero;
    private int ppu;
    private Client client;
    private Vehicle vehicle;


    public Sale(short position, short ok, short manguera, double volumen, int dinero, int ppu) {
        this.position = position;
        this.ok = ok;
        this.manguera = manguera;
        this.volumen = volumen;
        this.dinero = dinero;
        this.ppu = ppu;
    }

    public short getPosition() {
        return position;
    }

    public short getOk() {
        return ok;
    }

    public short getManguera() {
        return manguera;
    }

    public double getVolumen() {
        return volumen;
    }

    public int getDinero() {
        return dinero;
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
        return  "Position: " + position +", ok: " + ok +", manguera: " + manguera +", volumen: " + volumen +", dinero: " + dinero +", ppu: " + ppu ;
    }
}
