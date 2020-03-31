package com.fcs.fcspos.io;

import java.util.HashMap;
import java.util.Map;

public class UseCases {


    public Map<String, String> initialSettingsEDS() {
        Map<String, String > mapEDS = new HashMap<>();
        mapEDS.put("NOMBRE", "EDS Los Narjanjos");
        mapEDS.put("NIT", "811009788-8");
        mapEDS.put("TELEFONO", "43729688");
        mapEDS.put("DIRECCION", "Cr42 54 A-35");
        mapEDS.put("CIUDAD_DEPARTAMENTO", "Itaguí - Antioquia");
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


    //Necesito clientes creditos

}
