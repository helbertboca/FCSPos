package com.fcs.fcspos.model;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class Receipt {

    private Station station;
    private Sale sale;
    private Programming programming;

    public Receipt(Station station, Sale sale, Programming programming) {
        this.station = station;
        this.sale = sale;
        this.programming = programming;
    }


    public String build(byte quantity){
        final String HEADER = "Estacion de servicio";
        final String SEPARATOR = "----------------------";
        String copy = "Copia";

        if(quantity<1){
            copy="  ";
        }
        return "\n\n" + addSpace(copy.length()) + copy + "\n" +
                addSpace(HEADER.length()) + HEADER + "\n" +
                addSpace(station.getNombre().length()) + station.getNombre() + "\n" +
                addSpace(("Nit: " + station.getNit()).length()) + "Nit: " + station.getNit() + "\n" +
                addSpace(("Tel:" + station.getTelefono()).length()) + "Tel:" + station.getTelefono() + "\n" +
                addSpace(("Dir:" + station.getDireccion()).length()) + "Dir:" + station.getDireccion() + "\n" +
                addSpace(SEPARATOR.length()) + SEPARATOR + "\n" +
                addSpace(("FECHA:" + deviceDate()).length()) + "FECHA:" + deviceDate() + "\n" +
                addSpace("Numero de recibo:".length()) + "Numero de recibo:" + "\n" +
                addSpace("EQUIPO - CARA - MANGUERA".length()) + "EQUIPO - CARA - MANGUERA" + "\n" +
                addSpace(("MFC045 - " + sale.getPosition() + " - " + sale.getManguera()).length()) + "MFC045 - " + sale.getPosition() + " - " + sale.getManguera() + "\n\n" +
                addSpace(("  ").length()) + "  " + "\n" +
                addSpace("CANTIDAD - PRODUCTO - PPU".length()) + "CANTIDAD - PRODUCTO - PPU" + "\n" +
                addSpace((sale.getVolumen() + " - " + programming.getProduct() + " - " + sale.getPpu()).length()) + sale.getVolumen() + " - " + programming.getProduct() + " - " + sale.getPpu() + "\n" +
                addSpace(SEPARATOR.length()) + SEPARATOR + "\n" +
                addSpace(("Placa: " + sale.getVehicle().getLicense_plate()).length()) + "Placa: " + sale.getVehicle().getLicense_plate() + "\n" +
                addSpace(("Empleado: ").length()) + "Empleado: " + "\n" + addSpace(SEPARATOR.length()) + SEPARATOR + "\n" +
                addSpace(("Total: $" + sale.getDinero()).length()) + "Total: $" + sale.getDinero() + "\n" +
                addSpace(("  ").length()) + "  " + "\n" +
                addSpace(("  ").length()) + "  " + "\n" +
                addSpace(("  ").length()) + "  " + "\n" +
                addSpace(("  ").length()) + "  " + "\n";
    }


    private String deviceDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault());
        if(dateFormat.format(new Date())==null){
            return "---- -- --";
        }
        return dateFormat.format(new Date());
    }


    private StringBuilder addSpace(int size){
        final byte PAPER_WIDTH= 32;
        int quantity = (PAPER_WIDTH - size )/2;

        StringBuilder strB = new StringBuilder();
        for (byte i=0; i<quantity; i++){
            strB.append(" ");
        }
        return strB;
    }






}
