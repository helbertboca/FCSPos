package com.fcs.fcspos.io;

import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

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
                try {
                    socket = new Socket(addressIpServer, port);
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.write(dataToMfc + "\n");
                    printWriter.flush();
                    answer = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                    setAnswer(answer);
                    printWriter.close();
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        SystemClock.sleep(140);
        return answer;
    }



}
