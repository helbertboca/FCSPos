package com.fcs.fcspos.model;

import android.print.PrinterId;

import java.util.ArrayList;

public class Dispenser {

    private String brand;
    private int numberOfDigits;
    private int decimalsInVolume;
    private ArrayList<Side> sides;


    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setNumberOfDigits(int numberOfDigits) {
        this.numberOfDigits = numberOfDigits;
    }

    public void setDecimalsInVolume(int decimalsInVolume) {
        this.decimalsInVolume = decimalsInVolume;
    }

    public void setSides(ArrayList<Side> sides) {
        this.sides = sides;
    }

    public ArrayList<Side> getSides() {
        return sides;
    }
}
