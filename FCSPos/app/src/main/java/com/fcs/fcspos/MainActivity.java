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
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Hose;
import com.fcs.fcspos.model.Net;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Side;
import com.fcs.fcspos.model.Station;
import com.fcs.fcspos.ui.activities.PositionActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    private Net net;
    private Programming programming;
    private Dispenser dispenser;
    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        station = new Station("EDS Los Narjanjos", "811009788-8", "43729688","Cr42 54 A-35" , "Itaguí - Antioquia");
        initialSettingsDispenser();
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


    private void establishConnection(){
        final byte OLD_CONNECTION=1, NEW_CONNECTION=2, ERROR_CONNECTION=0;
        switch (MfcWifiCom.conectar(getApplicationContext(), net)){
            case OLD_CONNECTION:
                new ConetionMfcThread().execute("5");
                break;
            case NEW_CONNECTION:
                new ConetionMfcThread().execute("32");
                break;
            case ERROR_CONNECTION:
                Toast.makeText(getApplicationContext(), "No puede conectarse con el equipo", Toast.LENGTH_SHORT).show();
        }
    }


    private void initialSettingsDispenser() {
        //Configuraciones parciales para pruebas de venta ------------------------------------------
        final String BRAND="Gilbarco";
        final byte NUMBER_OF_DIGITS=6, DECIMALS_IN_VOLUME=3;
        final byte NUMBER_OF_FACES=2, NUMBER_OF_HOUSES_PERFACE=3;
        final byte SIDE_A=0, SIDE_B=1;
        short[] ppus = {7000,8000,10500};
        //------------------------------------------------------------------------------------------
        dispenser = new Dispenser(BRAND , NUMBER_OF_DIGITS, DECIMALS_IN_VOLUME);
        ArrayList<Side> sides = new ArrayList<>();
        for(int x=0; x<NUMBER_OF_FACES; x++){
            sides.add(new Side());
        }
        ArrayList<Hose> hosesLA = new ArrayList<>();
        for(int x=0; x<NUMBER_OF_HOUSES_PERFACE;x++){
            hosesLA.add(new Hose(ppus[x]));
        }
        sides.get(SIDE_A).setHoses(hosesLA);
        ArrayList<Hose> hosesLB = new ArrayList<>();
        for(int x=0; x<NUMBER_OF_HOUSES_PERFACE;x++){
            hosesLB.add(new Hose(ppus[x]));
        }
        sides.get(SIDE_B).setHoses(hosesLB);
        dispenser.setSides(sides);
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
            SystemClock.sleep(80);
            return appMfcProtocol.getEstado() != 0;
        }

        @Override
        protected void onPostExecute(Boolean respuesta){
            progressBar.setVisibility(View.GONE);
            if(respuesta){
                Intent i = new Intent(getApplicationContext(), PositionActivity.class);
                i.putExtra("AppMfcProtocol", appMfcProtocol);
                i.putExtra("net", net);
                i.putExtra("dispenser", dispenser);
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
