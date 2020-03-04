package com.fcs.fcspos.model;


import java.io.Serializable;

public class Configuration implements Serializable {


    public Configuration(){ }

    public byte getCod_ERROR() {
        return 0;
    }

    public byte getCod_ESPERA() {
        return 6;
    }

    public byte getCod_LISTO() {
        return 7;
    }

    public byte getCod_AUTORIZADO() {
        return 8;
    }

    public byte getCod_SURTIENDO() {
        return 9;
    }

    public byte getCod_VENTA() {
        return 10;
    }
}
