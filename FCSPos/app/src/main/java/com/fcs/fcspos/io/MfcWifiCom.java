package com.fcs.fcspos.io;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.SystemClock;

import com.fcs.fcspos.model.Net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;


public class MfcWifiCom implements Serializable {

    private String addressIpServer;
    private int port;
    private String answer;
    private static MfcWifiCom mfcWifiCom;


    private MfcWifiCom(String addressIpServer, int port) {
        this.addressIpServer = addressIpServer;
        this.port = port;
    }

    public static MfcWifiCom getInstance(String addressIpServer, int port){
        if(mfcWifiCom ==null){
            mfcWifiCom = new MfcWifiCom(addressIpServer, port);
        }
        return mfcWifiCom;
    }

    @Override
    public MfcWifiCom clone(){
        try {
            throw new CloneNotSupportedException();
        } catch (CloneNotSupportedException ex) {
            System.out.println("No se puede clonar un objeto de la clase");
        }
        return null;
    }


    public void sendRequest(final String dataToMfc) {
        final Socket[] socket = new Socket[1];
        answer = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter printWriter=null;
                InputStreamReader inputStreamReader=null;
                try {
                    socket[0] = new Socket(addressIpServer, port);
                    printWriter = new PrintWriter(socket[0].getOutputStream());
                    printWriter.write(dataToMfc + "\n");
                    printWriter.flush();
                    inputStreamReader = new InputStreamReader(socket[0].getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    answer = bufferedReader.readLine();
                    setAnswer(answer);
                    socket[0].close();
                }catch (IOException e){
                    System.out.println("Inconveniente de Lectura nula");
                    setAnswer("");
                }finally {
                    try {
                        if (inputStreamReader != null) {
                            inputStreamReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


    public static byte conectar(Context context, Net net) {
        final byte OLD_CONNECTION=1, NEW_CONNECTION=2, ERROR_CONNECTION=0;

        WifiConfiguration conf = new WifiConfiguration();
        WifiManager wifiManager = configureWifi(conf, context, net);
        if (wifiManager != null) {
            if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
                wifiManager.addNetwork(conf);
                List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                if(list!= null){
                    for( WifiConfiguration i : list ) {
                        if(i.SSID != null && i.SSID.equals("\"" + net.getSsid() + "\"")) {
                            if(wifiManager.getConnectionInfo().getSSID().equals("\"" + net.getSsid() + "\"")) {
                                wifiManager.enableNetwork(i.networkId, true);
                                wifiManager.reconnect();
                                return OLD_CONNECTION;
                            }else {
                                wifiManager.disconnect();
                                wifiManager.enableNetwork(i.networkId, true);
                                wifiManager.reconnect();
                                return NEW_CONNECTION;
                            }
                        }
                    }
                }else{
                    System.out.println("Lista vacia");
                }
            }else{
                System.out.println("Active el wifi por favor");
            }
        }
        return ERROR_CONNECTION;
    }


    private static WifiManager configureWifi(WifiConfiguration conf,Context context, Net net){
        conf.SSID = "\"" + net.getSsid() + "\"";
        conf.preSharedKey = "\""+ net.getPassword() +"\"";
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        conf.preSharedKey = "\"".concat(net.getPassword()).concat("\"");
        return (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }


    private void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        SystemClock.sleep(170);
        return answer;
    }

}
