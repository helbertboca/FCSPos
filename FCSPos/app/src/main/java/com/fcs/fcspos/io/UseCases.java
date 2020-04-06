package com.fcs.fcspos.io;

import java.util.HashMap;
import java.util.Map;

public class UseCases {


    public Map<String, String> initialSettingsEDS() {
        Map<String, String > mapEDS = new HashMap<>();
        mapEDS.put("NAME", "EDS Los Narjanjos");
        mapEDS.put("NIT", "811009788-8");
        mapEDS.put("PHONE", "43729688");
        mapEDS.put("DIRECTION", "Cr42 54 A-35");
        mapEDS.put("CITY_DEPARTMENT", "Itaguí - Antioquia");
        return mapEDS;
    }


    public Map<String, String> initialSettingsDispenser(){
        Map<String, String > mapDispenser = new HashMap<>();
        mapDispenser.put("BRAND", "GILBARCO");
        mapDispenser.put("NUMBER_OF_DIGITS", "6");
        mapDispenser.put("DECIMALS_IN_VOLUME", "3");
        mapDispenser.put("NUMBER_OF_FACES", "2");
        mapDispenser.put("NUMBER_OF_HOUSES_PERFACE", "3");
        mapDispenser.put("PPU1", "7000");
        mapDispenser.put("PPU2", "8000");
        mapDispenser.put("PPU3", "10000");
        return mapDispenser;
    }

    public Map<String, String> customerCredit() {
        Map<String, String> mapClient = new HashMap<>();

        mapClient.put("MESSAGE", "Cliente de AXA Seguros Colpatria vehiculos" +
                ", cupo disponible de $15.000 con ppu de $8200 ,extra");
        mapClient.put("PRODUCT","1");//producto por numero, extra=1, corriente=2, diesel=3
        mapClient.put("PPU", "8200");
        mapClient.put("NAME", "Julio Cardenas De los Valdez");
        mapClient.put("MONEY", "15000");
        mapClient.put("VOLUME", "1.82");
        return mapClient;
    }


    public void saleCounted(String sale, String client, String vehicle) {
        System.out.println(sale);
        System.out.println(client);
        System.out.println(vehicle);
    }

    public void saleCredit(String sale, String client, String vehicle, String identification) {
        System.out.println(sale);
        System.out.println(client);
        System.out.println(vehicle);
        System.out.println(identification);
    }

    //falta recibo de venta credito de la capa superior
    //falta totales electronicos peticion de la capa superior


}
