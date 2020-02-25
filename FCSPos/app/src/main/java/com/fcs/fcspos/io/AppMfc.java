package com.fcs.fcspos.io;

import android.os.SystemClock;

import com.fcs.fcspos.model.Programming;


public class AppMfc {

    private MfcWifi mfcWifi;
    private Programming programming;
    private short estado;


    public AppMfc(MfcWifi mfcWifi){
        this.mfcWifi = mfcWifi;
    }


    public void machineCommunication(){
        final int ERROR=0, ESPERA=6, LISTO=7, AUTORIZADO=8, SURTIENDO=9, VENTA=10;
        final int OK = 1;
        final String SEPARATOR=";";
        boolean ventaTerminada=false;
        
        //do {
            String[] splitAnswer;
            mfcWifi.sendRequest("estado;" + programming.getPosition());//pido estado
            if (mfcWifi.getAnswer() != null) {
                System.out.println("Respuesta estado: " + mfcWifi.getAnswer());
                splitAnswer = mfcWifi.getAnswer().split(SEPARATOR);
                if(splitAnswer.length>2){
                    if (splitAnswer[2].equals("A")) {
                        splitAnswer[2] = "10";
                    }
                    switch (Integer.parseInt(splitAnswer[2])) {
                        case ESPERA:
                            System.out.println("ESTADO ESPERA");
                            estado = ESPERA;
                            if(programming!=null){//si hay venta programada, realizarla
                                mfcWifi.sendRequest("programar;"+ programming.getPosition()
                                        +";M" + programming.getProduct() + ";T" + programming.getPresetKind()
                                        + ";P" + programming.getQuantity());
                                SystemClock.sleep(140);
                                if (mfcWifi.getAnswer() != null) {
                                    for(int i=0; i<splitAnswer.length; i++){
                                        splitAnswer[i]="";
                                    }
                                    splitAnswer = mfcWifi.getAnswer().split(SEPARATOR);
                                    if (Integer.parseInt(splitAnswer[2]) == OK) {
                                        System.out.println("SE PROGRAMO");
                                    } else {
                                        System.out.println("error en la programacion");
                                    }
                                } else {
                                    System.out.println("No hubo respuesta de la programacion");
                                }
                            }
                            break;
                        case LISTO:
                            System.out.println("ESTADO LISTO");
                            estado = LISTO;
                            mfcWifi.sendRequest("autorizar;" + programming.getPosition());
                            if (mfcWifi.getAnswer() != null) {
                                for(int i=0; i<splitAnswer.length; i++){
                                    splitAnswer[i]="";
                                }
                                splitAnswer = mfcWifi.getAnswer().split(SEPARATOR);
                                if (Integer.parseInt(splitAnswer[2]) == OK) {
                                    System.out.println("SE AUTORIZO");
                                } else {
                                    System.out.println("error en la autorizacion");
                                }
                            } else {
                                System.out.println("No hubo respuesta de autorizacion");
                            }
                            break;
                        case AUTORIZADO:
                            System.out.println("ESTADO AUTORIZADO");
                            estado = AUTORIZADO;
                            break;
                        case SURTIENDO:
                            System.out.println("ESTADO SURTIENDO");
                            estado = SURTIENDO;
                            break;
                        case VENTA:
                            System.out.println("ESTADO VENTA");
                            estado = VENTA;
                            mfcWifi.sendRequest("venta;" + programming.getPosition());
                            if (mfcWifi.getAnswer() != null) {
                                final String[] splitAnswerS = mfcWifi.getAnswer().split(SEPARATOR);

                            }
                            ventaTerminada = true;
                            break;
                        case ERROR:
                            System.out.println("ESTADO ERROR");
                            estado = LISTO;
                            break;
                    }
                }
            }else {
                System.out.println("NULL EN ESTADO");
            }

        //} while (!ventaTerminada);

        //System.out.println("FIN TEMPORAL");

    }

    public void setProgramming(Programming programming) {
        this.programming = programming;
    }

    public short getEstado() {
        return estado;
    }
}
