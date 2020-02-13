package com.fcs.fcspos.io;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.fcs.fcspos.ui.activities.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class MfcWifi {

    private String networkSSID;
    private String networkPass;
    private String addressIpServer;
    private int port;
    private Socket socket;
    private String answer;
    private WifiManager wifiManager;
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

    public boolean conectionMfcWifi(Context context){
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";
        conf.preSharedKey = "\""+ networkPass +"\"";
        wifiManager = (WifiManager)context.getApplicationContext().getSystemService(MainActivity.WIFI_SERVICE);
        if(wifiManager!=null){
            if ((!wifiManager.isWifiEnabled())) {
                Toast.makeText(context, "Conectando a WIFI...", Toast.LENGTH_LONG).show();
                wifiManager.setWifiEnabled(true);//depreciado apartir de la api 29
                return false;
            }
            wifiManager.addNetwork(conf);
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    return true;
                }
            }
            return false;
        }else{
            System.out.println("el manejador esta nulo");
            return false;
        }
    }

    public void sendRequest(final String dataToMfc) {
        answer=null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(addressIpServer, port);
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.write(dataToMfc + "\n");
                    printWriter.flush();
                    answer = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                    printWriter.close();
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String getAnswer() {
        return answer;
    }

    public boolean okConection(){
        return wifiManager.isWifiEnabled();
    }


}
