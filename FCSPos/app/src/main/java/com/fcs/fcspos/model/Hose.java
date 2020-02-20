package com.fcs.fcspos.model;

public class Hose {

    private byte state;
    private int electronicTotal;
    private short ppu;

    public Hose(short ppu){
        this.ppu = ppu;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public byte getState() {
        return state;
    }

    public void setPpu(short ppu) {
        this.ppu = ppu;
    }

    public short getPpu() {
        return ppu;
    }

    public void setElectronicTotal(int electronicTotal) {
        this.electronicTotal = electronicTotal;
    }

    public int getElectronicTotal() {
        return electronicTotal;
    }
}
