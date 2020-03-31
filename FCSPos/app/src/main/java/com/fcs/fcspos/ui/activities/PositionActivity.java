package com.fcs.fcspos.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fcs.fcspos.MainActivity;
import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.model.Net;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Station;
import com.fcs.fcspos.model.Vehicle;
import com.google.gson.Gson;


public class PositionActivity extends AppCompatActivity {

    private LinearLayout llSales, llBasket, llRecord, llTurn, llCablibrate;
    private AppMfcProtocol appMfcProtocol;
    private Net net;
    private Station station;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        appMfcProtocol = (AppMfcProtocol) getIntent().getSerializableExtra("AppMfcProtocol");
        net = (Net)getIntent().getSerializableExtra("net");
        station = (Station)getIntent().getSerializableExtra("station");
        supplierStatus();
        initView();
        eventsViews();
    }


    private void supplierStatus(){
        appMfcProtocol.machineCommunication(false);
        SystemClock.sleep(80);
        if(appMfcProtocol.getEstado()==  appMfcProtocol.getDispenser().getCod_LISTO()){
            Toast.makeText(getApplicationContext(), "Manguera levantada", Toast.LENGTH_SHORT).show();
        }else if(appMfcProtocol.getEstado()== appMfcProtocol.getDispenser().getCod_SURTIENDO()){
            Toast.makeText(getApplicationContext(), "Surtiendo", Toast.LENGTH_SHORT).show();
            pendingSale( appMfcProtocol.getDispenser().getCod_SURTIENDO());
        }else if(appMfcProtocol.getEstado()== appMfcProtocol.getDispenser().getCod_VENTA()){
            pendingSale( appMfcProtocol.getDispenser().getCod_VENTA());
        }else if(appMfcProtocol.getEstado()== appMfcProtocol.getDispenser().getCod_ERROR()){
            Toast.makeText(getApplicationContext(), "No hay respuesta de la MFC", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void pendingSale(int currentProcess){
        final SharedPreferences sharedPref = PositionActivity.this.getSharedPreferences("pendingSales", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = sharedPref.getString(net.getSsid() + "/" + appMfcProtocol.getProgramming().getPosition(),null);
        Programming programming = gson.fromJson(json, Programming.class);//
        appMfcProtocol.setProgramming(programming);
        if(json!=null){
            validateActiveSale((byte) currentProcess, true);
        }else {
            validateActiveSale((byte) currentProcess, false);
        }
    }


    private void validateActiveSale(byte currentProcess, boolean pendingSale){
        if(pendingSale){
            salesActivity(currentProcess);
        }else{
            Toast.makeText(getApplicationContext(),"Esta venta no le pertenece a este dispositivo", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void salesActivity(byte currentProcess){
        Intent i = new Intent(getApplicationContext(), SalesActivity.class);
        i.putExtra("currentProcess", currentProcess);
        i.putExtra("appMfcProtocol", appMfcProtocol);
        i.putExtra("net", net);
        i.putExtra("station", station);
        startActivity(i);
    }

    private void eventsViews() {
        llSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appMfcProtocol.getProgramming().setVehicle(new Vehicle());
                salesActivity( appMfcProtocol.getDispenser().getCod_ESPERA());
            }
        });
        llBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        llRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        llTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        llCablibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void initView() {
        llSales = findViewById(R.id.llSales);
        llBasket = findViewById(R.id.llBasket);
        llRecord = findViewById(R.id.llRecord);
        llTurn = findViewById(R.id.llTurn);
        llCablibrate = findViewById(R.id.llCablibrate);
    }
}
