package com.fcs.fcspos.model;


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

        StringBuilder receipt= new StringBuilder();
        //for(byte i=0; i<quantity; i++){
            receipt.append("\n\n").append(addSpace(HEADER.length())).append(HEADER).append("\n").
                    append(addSpace(station.getNombre().length())).append(station.getNombre()).append("\n").
                    append(addSpace(("Nit: " + station.getNit()).length())).append("Nit: ").append(station.getNit()).append("\n").
                    append(addSpace(("Tel:" + station.getTelefono()).length())).append("Tel:").append(station.getTelefono()).append("\n").
                    append(addSpace(("Dir:" + station.getDireccion()).length())).append("Dir:").append(station.getDireccion()).append("\n").
                    append(addSpace(SEPARATOR.length())).append(SEPARATOR).append("\n").
                    append(addSpace("FECHA:".length())).append("FECHA:").append("\n").
                    append(addSpace("Numero de recibo:".length())).append("Numero de recibo:").append("\n").
                    append(addSpace("EQUIPO - CARA - MANGUERA".length())).append("EQUIPO - CARA - MANGUERA").append("\n").
                    append(addSpace(("MFC045 - " + sale.getPosition() + " - " + sale.getManguera()).length())).append("MFC045 - ").append(sale.getPosition()).append(" - ").append(sale.getManguera()).append("\n\n").
                    append(addSpace(("  ").length())).append("  ").append("\n").
                    append(addSpace("CANTIDAD - PRODUCTO - PPU".length())).append("CANTIDAD - PRODUCTO - PPU").append("\n").
                    append(addSpace((sale.getVolumen() + " - " + programming.getProduct() + " - " + sale.getPpu()).length())).append(sale.getVolumen()).append(" - ").append(programming.getProduct()).append(" - ").append(sale.getPpu()).append("\n").
                    append(addSpace(SEPARATOR.length())).append(SEPARATOR).append("\n").
                    append(addSpace(("Placa: " + sale.getVehicle().getLicense_plate()).length())).append("Placa: ").append(sale.getVehicle().getLicense_plate()).append("\n").
                    append(addSpace(("Empleado: ").length())).append("Empleado: ").append("\n").append(addSpace(SEPARATOR.length())).append(SEPARATOR).append("\n").
                    append(addSpace(("Total: $" + sale.getDinero()).length())).append("Total: $").append(sale.getDinero()).append("\n").
                    append(addSpace(("  ").length())).append("  ").append("\n").
                    append(addSpace(("  ").length())).append("  ").append("\n").
                    append(addSpace(("  ").length())).append("  ").append("\n").
                    append(addSpace(("  ").length())).append("  ").append("\n");

        //}
        return receipt.toString();
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
