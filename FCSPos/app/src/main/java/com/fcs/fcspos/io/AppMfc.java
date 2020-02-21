package com.fcs.fcspos.io;

import android.os.SystemClock;


public class AppMfc {

    private MfcWifi mfcWifi;


    public AppMfc(MfcWifi mfcWifi){
        this.mfcWifi = mfcWifi;
    }


    public void machineCommunication(){
        final int ERROR=0, ESPERA=6, LISTO=7, AUTORIZADO=8, SURTIENDO=9, VENTA=10;
        final int OK = 1;
        boolean ventaTerminada=false;
        do {
            mfcWifi.sendRequest("estado;1");//pido estado
            if (mfcWifi.getAnswer() != null) {
                System.out.println("Respuesta estado: " + mfcWifi.getAnswer());
                final String[] splitAnswer = mfcWifi.getAnswer().split(";");
                if (splitAnswer[2].equals("A")) {
                    splitAnswer[2] = "10";
                }

                switch (Integer.parseInt(splitAnswer[2])) {
                    case ESPERA:
                        //mfcWifi.sendRequest("manguera;1");//manguera
                        //if(mfcWifi.getAnswer()!=null) {
                        //final String[] splitAnswerManguera = mfcWifi.getAnswer().split(";");
                        //if (Integer.parseInt(splitAnswerManguera[2]) == OK) {
                        mfcWifi.sendRequest("programar;1;M1;T2;P12000");
                        SystemClock.sleep(140);
                        if (mfcWifi.getAnswer() != null) {
                            final String[] splitProgramacion = mfcWifi.getAnswer().split(";");
                            if (Integer.parseInt(splitProgramacion[2]) == OK) {
                                mfcWifi.sendRequest("autorizar;1");
                                if (mfcWifi.getAnswer() != null) {
                                    final String[] splitAnswerAutorizacion = mfcWifi.getAnswer().split(";");
                                    if (Integer.parseInt(splitAnswerAutorizacion[2]) == OK) {
                                        System.out.println("SE AUTORIZO");
                                    } else {
                                        System.out.println("error en la autorizacion");
                                    }
                                } else {
                                    System.out.println("No hubo respuesta de autorizacion");
                                }
                            } else {
                                System.out.println("error en la programacion");
                            }
                        } else {
                            System.out.println("No hubo respuesta de la programacion");
                        }
                        //}else {
                        //    System.out.println("error en la manguera");
                        //}
                        //}else{
                        //    System.out.println("No hubo respuesta de la manguera");
                        //}
                        break;
                    case LISTO:
                        System.out.println("ESTADO LISTO");

                        break;
                    case AUTORIZADO:
                        System.out.println("ESTADO AUTORIZADO");

                        break;
                    case SURTIENDO:
                        System.out.println("ESTADO SURTIENDO");

                        break;
                    case VENTA:
                        System.out.println("ESTADO VENTA");


                        mfcWifi.sendRequest("venta;1");
                        if (mfcWifi.getAnswer() != null) {
                            System.out.println("Respuesta Venta: " + mfcWifi.getAnswer());
                            final String[] splitAnswerS = mfcWifi.getAnswer().split(";");

                        }


                        ventaTerminada = true;
                        break;
                    case ERROR:
                        System.out.println("ESTADO ERROR");

                        break;

                }
            }
            System.out.println("NULL EN ESTADO");

        } while (!ventaTerminada);

        System.out.println("FIN TEMPORAL");


    }





}
