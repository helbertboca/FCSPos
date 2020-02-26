package com.fcs.fcspos.io;

import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;


public class MfcWifi implements Serializable {

    private String networkSSID;
    private String networkPass;
    private String addressIpServer;
    private int port;
    private Socket socket;
    private String answer;
    private static MfcWifi mfcWifi;


    private MfcWifi(String networkSSID, String networkPass, String addressIpServer, int port) {
        this.networkSSID = networkSSID;
        this.networkPass = networkPass;
        this.addressIpServer = addressIpServer;
        this.port = port;
    }

    public static MfcWifi getInstance(String networkSSID, String networkPass, String addressIpServer, int port){
        if(mfcWifi==null){
            mfcWifi = new MfcWifi(networkSSID, networkPass, addressIpServer, port);
        }
        return mfcWifi;
    }

    @Override
    public MfcWifi clone(){
        try {
            throw new CloneNotSupportedException();
        } catch (CloneNotSupportedException ex) {
            System.out.println("No se puede clonar un objeto de la clase");
        }
        return null;
    }


    public void sendRequest(final String dataToMfc) {

        answer = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter printWriter=null;
                InputStreamReader inputStreamReader=null;
                try {
                    socket = new Socket(addressIpServer, port);
                    printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.write(dataToMfc + "\n");
                    printWriter.flush();
                    inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    answer = bufferedReader.readLine();
                    setAnswer(answer);
                    socket.close();
                }catch (IOException e){
                    System.out.println("Inconveniente de Lectura nula");
                    setAnswer("");
                }finally {
                    try {
                        Objects.requireNonNull(inputStreamReader).close();//revisar
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Objects.requireNonNull(printWriter).close();
                }
            }
        }).start();

    }


    private void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        SystemClock.sleep(170);
        return answer;
    }



}
