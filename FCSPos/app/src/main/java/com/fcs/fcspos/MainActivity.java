package com.fcs.fcspos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.io.MfcWifiCom;
import com.fcs.fcspos.model.Configuration;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Net;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Station;
import com.fcs.fcspos.ui.activities.PositionActivity;



public class MainActivity extends AppCompatActivity{

    private Net net;
    private Programming programming;
    private Dispenser dispenser;
    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialSettings();
        Button btnPos1 = findViewById(R.id.btnPos1);
        Button btnPos2 = findViewById(R.id.btnPos2);
        Button btnPos3 = findViewById(R.id.btnPos3);
        Button btnPos4 = findViewById(R.id.btnPos4);

        btnPos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                programming = new Programming();
                programming.setPosition((byte) 1);//esta posicion tiene que ir determinada por los botones
                net = new Net("ESP32","123456789","192.168.4.1",80);
                establishConnection();
            }
        });
        btnPos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                programming = new Programming();
                programming.setPosition((byte) 2);//esta posicion tiene que ir determinada por los botones
                net = new Net("ESP32","123456789","192.168.4.1",80);
                establishConnection();
            }
        });
        btnPos3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                programming = new Programming();
                programming.setPosition((byte) 1);//esta posicion tiene que ir determinada por los botones
                net = new Net("ESP33","123456780","192.168.4.1",80);
                establishConnection();
            }
        });
        btnPos4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                programming = new Programming();
                programming.setPosition((byte) 2);//esta posicion tiene que ir determinada por los botones
                net = new Net("ESP33","123456780","192.168.4.1",80);
                establishConnection();
            }
        });

    }

    private void initialSettings() {
        station = Configuration.settingsEDS();
        dispenser = Configuration.settingsDispenser();
    }


    private void establishConnection(){
        final byte OLD_CONNECTION=1, NEW_CONNECTION=2, ERROR_CONNECTION=0;
        switch (MfcWifiCom.conectar(getApplicationContext(), net)){
            case OLD_CONNECTION:
                new ConetionMfcThread().execute("0");
                break;
            case NEW_CONNECTION:
                new ConetionMfcThread().execute("32");
                break;
            case ERROR_CONNECTION:
                Toast.makeText(getApplicationContext(), "No puede conectarse con el equipo", Toast.LENGTH_SHORT).show();
        }
    }

    //----------------------------------------------------------------------------------------------
    private class ConetionMfcThread extends AsyncTask<String, Void, Boolean> {

        private ProgressBar progressBar;
        private AppMfcProtocol appMfcProtocol;

        protected void onPreExecute(){
            progressBar = findViewById(R.id.pbConetionWifi);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String ... attempts){
            int count=0;
            while ( Integer.parseInt(attempts[0])>count){
                System.out.println("conectandose con el equipo..." + count);
                SystemClock.sleep(200);
                count++;
            }
            appMfcProtocol = new AppMfcProtocol( MfcWifiCom.getInstance(net.getIp(), net.getPort()) ,dispenser);
            appMfcProtocol.setProgramming(programming);
            appMfcProtocol.machineCommunication(false);
            SystemClock.sleep(150);
            return appMfcProtocol.getEstado() != 0;
        }

        @Override
        protected void onPostExecute(Boolean answer){
            progressBar.setVisibility(View.GONE);
            if(answer){
                Intent i = new Intent(getApplicationContext(), PositionActivity.class);
                i.putExtra("AppMfcProtocol", appMfcProtocol);
                i.putExtra("net", net);
                i.putExtra("station", station);
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "No puede conectarse con el equipo", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {}


}
