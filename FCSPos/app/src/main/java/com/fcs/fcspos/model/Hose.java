package com.fcs.fcspos.model;

import java.io.Serializable;

public class Hose implements Serializable {

    private int electronicTotalMoney;
    private int electronicTotalVolume;
    private short ppu;

    public Hose(short ppu){
        this.ppu = ppu;
    }

    public void setPpu(short ppu) {
        this.ppu = ppu;
    }

    public short getPpu() {
        return ppu;
    }

    public int getElectronicTotalMoney() {
        return electronicTotalMoney;
    }

    public void setElectronicTotalMoney(int electronicTotalMoney) {
        this.electronicTotalMoney = electronicTotalMoney;
    }

    public int getElectronicTotalVolume() {
        return electronicTotalVolume;
    }

    public void setElectronicTotalVolume(int electronicTotalVolume) {
        this.electronicTotalVolume = electronicTotalVolume;
    }
}
