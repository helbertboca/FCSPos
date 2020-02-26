package com.fcs.fcspos.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Client implements Serializable {
    private String identificationCard;
    private String nit;

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
        return "Cliente: identificacion:" + identificationCard + ", nit:" + nit;
    }
}
