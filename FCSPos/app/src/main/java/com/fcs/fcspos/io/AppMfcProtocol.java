package com.fcs.fcspos.io;

import android.os.SystemClock;

import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Sale;

import java.io.Serializable;


public class AppMfcProtocol implements Serializable {

    private MfcWifiCom mfcWifiCom;
    private Programming programming;
    private byte estado;
    private final String SEPARATOR=";";
    private boolean correctHose=false;
    private Dispenser dispenser;
    private final int OK = 1;


    public AppMfcProtocol(MfcWifiCom mfcWifiCom, Dispenser dispenser){
        this.mfcWifiCom = mfcWifiCom;
        this.dispenser = dispenser;
    }

    public void machineCommunication(boolean pendingSale){
        String[] splitAnswer;
        final String STATE="estado";

        int count=0;
        do{
            System.out.println(programming.getPosition() + "pos");
            mfcWifiCom.sendRequest(STATE + SEPARATOR + programming.getPosition());//pido estado
            SystemClock.sleep(100);
            count++;
        }while (!mfcWifiCom.isData() && count<4);

        if (mfcWifiCom.getAnswer() != null) {
            System.out.println("Respuesta estado: " + mfcWifiCom.getAnswer());
            splitAnswer = mfcWifiCom.getAnswer().split(SEPARATOR);
            if(splitAnswer.length>2){
                if (splitAnswer[2].equals("A")) {
                    splitAnswer[2] = "10";
                }
                if(splitAnswer[0].equals(STATE) && Integer.parseInt(splitAnswer[1]) == programming.getPosition()) {
                    if(Integer.parseInt(splitAnswer[2]) == dispenser.getCod_ESPERA()){
                        System.out.println("ESTADO ESPERA");
                        estado = dispenser.getCod_ESPERA();
                    }else if(Integer.parseInt(splitAnswer[2]) == dispenser.getCod_LISTO()){ //LISTO
                        System.out.println("ESTADO LISTO");
                        estado = dispenser.getCod_LISTO();
                        processReady(pendingSale, splitAnswer);
                    }else if(Integer.parseInt(splitAnswer[2]) == dispenser.getCod_AUTORIZADO()){ //AUTORIZADO
                        System.out.println("ESTADO AUTORIZADO");
                        estado = dispenser.getCod_AUTORIZADO();
                    }else if(Integer.parseInt(splitAnswer[2]) == dispenser.getCod_SURTIENDO()){ //SURTIENDO
                        System.out.println("ESTADO SURTIENDO");
                        estado = dispenser.getCod_SURTIENDO();
                    }else if(Integer.parseInt(splitAnswer[2]) == dispenser.getCod_VENTA()){ //VENTA
                        System.out.println("ESTADO VENTA");
                        estado = dispenser.getCod_VENTA();
                    }else if(Integer.parseInt(splitAnswer[2]) == dispenser.getCod_ERROR()){ //ERROR
                        System.out.println("ESTADO ERROR");
                        estado = dispenser.getCod_ERROR();
                    }
                }
            }
        }else {
            System.out.println("NULL EN ESTADO");
        }
    }


    private void processReady(boolean pendingSale, String[] splitAnswer) {
        if(programming.getKind()!=null && !pendingSale) {//si hay venta programada, realizarla
            scheduledSale(splitAnswer);
        }else{
            unscheduledSale(splitAnswer);
        }
    }


    private void scheduledSale(String[] splitAnswer) {
        final String AUTHORIZE="autorizar";

        mfcWifiCom.sendRequest(AUTHORIZE + SEPARATOR + programming.getPosition());
        if (mfcWifiCom.getAnswer() != null) {
            for (int i = 0; i < splitAnswer.length; i++) {
                splitAnswer[i] = "";
            }
            splitAnswer = mfcWifiCom.getAnswer().split(SEPARATOR);
            if (splitAnswer[0].equals(AUTHORIZE) && Integer.parseInt(splitAnswer[1])==programming.getPosition()
                    && Integer.parseInt(splitAnswer[2]) == OK) {
                System.out.println("SE AUTORIZO");
            } else {
                System.out.println("error en la autorizacion");
            }
        } else {
            System.out.println("No hubo respuesta de autorizacion");
        }
    }


    private void unscheduledSale(String[] splitAnswer) {
        final String HOSE="manguera", PROGRAM="programar";

        mfcWifiCom.sendRequest(HOSE + SEPARATOR + programming.getPosition());
        if(mfcWifiCom.getAnswer()!= null){
            for (int i = 0; i < splitAnswer.length; i++) {
                splitAnswer[i] = "";
            }
            splitAnswer = mfcWifiCom.getAnswer().split(SEPARATOR);
            if (splitAnswer[0].equals(HOSE) && Integer.parseInt(splitAnswer[1])==programming.getPosition()
                    && Integer.parseInt(splitAnswer[2]) == programming.getProduct()) {
                System.out.println("Manguera correcta");
                setCorrectHose(true);
                mfcWifiCom.sendRequest(PROGRAM + SEPARATOR + programming.getPosition()
                        +";M" + programming.getProduct() + ";T" + programming.getPresetKind()
                        + ";P" + programming.getQuantity());
                SystemClock.sleep(140);
                if (mfcWifiCom.getAnswer() != null) {
                    for(int i=0; i<splitAnswer.length; i++){
                        splitAnswer[i]="";
                    }
                    splitAnswer = mfcWifiCom.getAnswer().split(SEPARATOR);
                    if (splitAnswer[0].equals(PROGRAM) && Integer.parseInt(splitAnswer[1])==programming.getPosition()
                            && Integer.parseInt(splitAnswer[2]) == OK) {
                        System.out.println("SE PROGRAMO");
                    } else {
                        System.out.println("error en la programacion");
                    }
                } else {
                    System.out.println("No hubo respuesta de la programacion");
                }
            } else {
                System.out.println("manguera incorrecta");
                setCorrectHose(false);
            }
        }
    }


    private double transformVolume(String volume) {
        int x =Integer.parseInt(volume);
        return Double.parseDouble((x/1000) + "." + (x%1000));
    }

    public Programming getProgramming() {
        return programming;
    }

    public void setProgramming(Programming programming) {
        this.programming = programming;
    }

    public byte getEstado() {
        return estado;
    }

    public Sale getSale() {
        final String SALE="venta";

        mfcWifiCom.sendRequest(SALE + SEPARATOR + programming.getPosition());
        if (mfcWifiCom.getAnswer() != null) {
            final String[] splitSale = mfcWifiCom.getAnswer().split(SEPARATOR);
            if(splitSale.length>5){
                return new Sale(Short.parseShort(splitSale[1]),
                        Short.parseShort(splitSale[2]),Short.parseShort(splitSale[3]),
                        transformVolume(splitSale[4]),Integer.parseInt(splitSale[5]),
                        Integer.parseInt(splitSale[6]) );
            }
        }
        return null;
    }

    public Dispenser getDispenser() {
        return dispenser;
    }

    public boolean isCorrectHose() {
        return correctHose;
    }

    private void setCorrectHose(boolean correctHose) {
        this.correctHose = correctHose;
    }
}
