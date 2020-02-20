package com.fcs.fcspos.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Side implements Serializable {

    private ArrayList<Hose> hoses;

    public void setHoses(ArrayList<Hose> hoses) {
        this.hoses = hoses;
    }

    public ArrayList<Hose> getHoses() {
        return hoses;
    }
}
