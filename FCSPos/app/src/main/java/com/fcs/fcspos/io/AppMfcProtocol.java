package com.fcs.fcspos.io;

import android.os.SystemClock;

import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Hose;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Sale;

import java.io.Serializable;


public class AppMfcProtocol implements Serializable {

    private MfcWifiCom mfcWifiCom;
    private Programming programming;
    private Client client;
    private Dispenser dispenser;
    private byte estado;
    private final String SEPARATOR=";";
    private final int OK = 1;
    private boolean correctHose=false;
    private final String PRICE="precio", HOSE= "M", TOTALS="totales";


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
        final String SALEKIND_COUNTED="Counted", SALEKIND_CREDIT="Credit";

        if(programming.getKind()!=null && !pendingSale) {//si hay venta programada, realizarla
            scheduledSale(splitAnswer);
        }else{
            if(programming.getKind().equals(SALEKIND_COUNTED)){
                unscheduledSale(splitAnswer);
            }else{
                if(changePrice(splitAnswer)){
                    unscheduledSale(splitAnswer);
                }
            }
        }
    }


    public Dispenser requestTotals(Dispenser dispenser, byte position) {
        mfcWifiCom.sendRequest(TOTALS + SEPARATOR + position + SEPARATOR + "M3");
        SystemClock.sleep(200);
        String[] splitAnswer = new String[16];
        splitAnswer = requestTotalsResponse(splitAnswer, position);
        if(splitAnswer!=null) {
            byte count=3;
            for (Hose h:dispenser.getSides().get(position).getHoses()) {
                h.setElectronicTotalVolume(Integer.parseInt(splitAnswer[count]));
                count++;
                h.setElectronicTotalMoney(Integer.parseInt(splitAnswer[count]));
                count++;
            }
            return dispenser;
        }
        return null;
    }


    private String[] requestTotalsResponse(String[] splitAnswer, byte position) {
        cleanBuffer(splitAnswer);
        if (mfcWifiCom.getAnswer() != null) {
            splitAnswer = mfcWifiCom.getAnswer().split(SEPARATOR);
            if (splitAnswer[0].equals(TOTALS) && Integer.parseInt(splitAnswer[1])==position
                    && Integer.parseInt(splitAnswer[2]) == OK) {
                System.out.println("Totales devueltos correctamente");
                return splitAnswer;
            } else {
                System.out.println("error en la peticion de totales");
                return null;
            }
        }else {
            System.out.println("No hubo respuesta de los totales");
            return null;
        }
    }



    private boolean changePrice(String[] splitAnswer){
        cleanBuffer(splitAnswer);
        mfcWifiCom.sendRequest(PRICE + SEPARATOR + programming.getPosition() + SEPARATOR +
                HOSE + client.getAuthorizedProduct() + SEPARATOR + "P" + client.getAuthorizedPpu());
        SystemClock.sleep(80);
        return priceChangeResponse(splitAnswer);
    }


    public boolean changePrice(byte position, int product, Dispenser dispenser){
        mfcWifiCom.sendRequest(PRICE + SEPARATOR + position + SEPARATOR + HOSE + product + SEPARATOR + "P" +
                (dispenser.getSides().get(position).getHoses().get(product).getPpu())  );
        SystemClock.sleep(80);
        String[] splitAnswer = new String[3];
        return priceChangeResponse(splitAnswer);
    }


    private boolean priceChangeResponse(String[] splitAnswer) {
        cleanBuffer(splitAnswer);
        if (mfcWifiCom.getAnswer() != null) {
            splitAnswer = mfcWifiCom.getAnswer().split(SEPARATOR);
            if (splitAnswer[0].equals(PRICE) && Integer.parseInt(splitAnswer[1])==programming.getPosition()
                    && Integer.parseInt(splitAnswer[2]) == OK) {
                System.out.println("Cambio de precio exitoso");
                return true;
            } else {
                System.out.println("error en el cambio de precio");
                return false;
            }
        }else {
            System.out.println("No hubo respuesta de cambio de precio");
            return false;
        }
    }


    private void cleanBuffer(String[] splitAnswer) {
        for (int i = 0; i < splitAnswer.length; i++) {
            splitAnswer[i] = "";
        }
    }


    private void scheduledSale(String[] splitAnswer) {
        final String AUTHORIZE="autorizar";

        cleanBuffer(splitAnswer);
        mfcWifiCom.sendRequest(AUTHORIZE + SEPARATOR + programming.getPosition());
        if (mfcWifiCom.getAnswer() != null) {
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

        cleanBuffer(splitAnswer);
        mfcWifiCom.sendRequest(HOSE + SEPARATOR + programming.getPosition());
        if(mfcWifiCom.getAnswer()!= null){
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
                    cleanBuffer(splitAnswer);
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


    public Programming getProgramming() {
        return programming;
    }

    public byte getEstado() {
        return estado;
    }

    public Dispenser getDispenser() {
        return dispenser;
    }

    public Client getClient() {
        return client;
    }


    public boolean isCorrectHose() {
        return correctHose;
    }

    public void setProgramming(Programming programming) {
        this.programming = programming;
    }

    private void setCorrectHose(boolean correctHose) {
        this.correctHose = correctHose;
    }

    public void setClient(Client client) {
        this.client = client;
    }



}
