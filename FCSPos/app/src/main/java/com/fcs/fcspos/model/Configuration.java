package com.fcs.fcspos.model;

import com.fcs.fcspos.io.UseCases;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Configuration {


    public static Station settingsEDS(){
        UseCases useCases  = new UseCases();
        Map<String, String> mapEDS =useCases.initialSettingsEDS();
        return new Station( Objects.requireNonNull(mapEDS.get("NOMBRE")),
                Objects.requireNonNull(mapEDS.get("NIT")),
                Objects.requireNonNull(mapEDS.get("TELEFONO")),
                Objects.requireNonNull(mapEDS.get("DIRECCION")),
                Objects.requireNonNull(mapEDS.get("CIUDAD_DEPARTAMENTO")));
    }

    public static Dispenser settingsDispenser() {
        UseCases useCases  = new UseCases();
        Map<String, String> mapDispenser =useCases.initialSettingsDispenser();
        Dispenser dispenser = new Dispenser(mapDispenser.get("BRAND"),
                (byte)Integer.parseInt(Objects.requireNonNull(mapDispenser.get("NUMBER_OF_DIGITS"))),
                (byte)Integer.parseInt(Objects.requireNonNull(mapDispenser.get("DECIMALS_IN_VOLUME"))));
        ArrayList<Side> sides = new ArrayList<>();
        for(int x=0; x< Integer.parseInt(Objects.requireNonNull(mapDispenser.get("NUMBER_OF_FACES"))); x++){
            sides.add(new Side());
        }
        short[] ppus = {(short)Integer.parseInt(Objects.requireNonNull(mapDispenser.get("PPU1"))),
                (short)Integer.parseInt(Objects.requireNonNull(mapDispenser.get("PPU2"))),
                (short)Integer.parseInt(Objects.requireNonNull(mapDispenser.get("PPU3")))};
        ArrayList<Hose> hosesLA = new ArrayList<>();
        for(int x=0; x< Integer.parseInt(Objects.requireNonNull(mapDispenser.get("NUMBER_OF_HOUSES_PERFACE")));x++){
            hosesLA.add(new Hose(ppus[x]));
        }
        final byte SIDE_A=0, SIDE_B=1;
        sides.get(SIDE_A).setHoses(hosesLA);
        ArrayList<Hose> hosesLB = new ArrayList<>();
        for(int x=0; x<Integer.parseInt(Objects.requireNonNull(mapDispenser.get("NUMBER_OF_HOUSES_PERFACE")));x++){
            hosesLB.add(new Hose(ppus[x]));
        }
        sides.get(SIDE_B).setHoses(hosesLB);
        dispenser.setSides(sides);
        return dispenser;
    }





}
