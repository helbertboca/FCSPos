package com.fcs.fcspos;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.MfcWifi;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Hose;
import com.fcs.fcspos.model.Side;
import com.fcs.fcspos.ui.activities.SalesActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{


    private Button btnSales,btnBasket,btnRecord,btnTurn,btnCalibrate;
    private MfcWifi mfcWifi;
    private Dispenser dispenser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mfcWifi = MfcWifi.getInstance("ESP32", "123456789", "192.168.4.1", 80);
        //mfcWifi = MfcWifi.getInstance("FCS_INVITADOS", "Fcs.inv*!||!", "192.168.102.29", 8080);
        initialSettingsDispenser();
        initView();
        eventsViews();

    }


    private void initialSettingsDispenser() {

        //Configuraciones parciales para pruebas de venta ------------------------------------------
        final String BRAND="Gilbarco";
        final byte NUMBER_OF_DIGITS=7, DECIMALS_IN_VOLUME=3;
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


    private void eventsViews() {
        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SalesActivity.class);
                startActivity(i);
            }
        });
        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void initView() {
        btnSales = findViewById(R.id.btnSales);
        btnBasket = findViewById(R.id.btnBasket);
        btnRecord = findViewById(R.id.btnRecord);
        btnTurn = findViewById(R.id.btnTurn);
        btnCalibrate = findViewById(R.id.btnCalibrate);
    }


}
