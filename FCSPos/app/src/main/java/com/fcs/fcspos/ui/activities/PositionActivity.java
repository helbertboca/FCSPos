package com.fcs.fcspos.ui.activities;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Hose;
import com.fcs.fcspos.model.Side;

import java.util.ArrayList;

public class PositionActivity extends AppCompatActivity {


    private Button btnSales,btnBasket,btnRecord,btnTurn,btnCalibrate;
    private Dispenser dispenser;
    private final byte ESPERA=6;
    private AppMfcProtocol appMfcProtocol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        initialSettingsDispenser();
        appMfcProtocol = (AppMfcProtocol) getIntent().getSerializableExtra("AppMfcProtocol");
        supplierStatus();
        initView();
        eventsViews();
    }


    private void supplierStatus(){
        final int ERROR=0, LISTO=7, AUTORIZADO=8, SURTIENDO=9, VENTA=10;
        appMfcProtocol.machineCommunication(false);
        SystemClock.sleep(80);
        switch (appMfcProtocol.getEstado()){
            case LISTO:
                Toast.makeText(getApplicationContext(), "Manguera levantada", Toast.LENGTH_SHORT).show();
                break;
            case SURTIENDO:
                Toast.makeText(getApplicationContext(), "Surtiendo", Toast.LENGTH_SHORT).show();
                nextActivity((byte) SURTIENDO);
                break;
            case VENTA:
                nextActivity((byte) VENTA);
                break;
            case ERROR:
                Toast.makeText(getApplicationContext(), "No hay respuesta de la MFC", Toast.LENGTH_SHORT).show();
                finish();
                break;
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

    private void nextActivity(byte currentProcess){
        Intent i = new Intent(getApplicationContext(), SalesActivity.class);
        i.putExtra("surtidor",dispenser);
        i.putExtra("currentProcess", currentProcess);
        i.putExtra("appMfcProtocol", appMfcProtocol);
        startActivity(i);
    }


    private void eventsViews() {
        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity((byte) ESPERA);
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
