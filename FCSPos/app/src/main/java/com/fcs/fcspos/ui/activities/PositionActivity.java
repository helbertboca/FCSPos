package com.fcs.fcspos.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fcs.fcspos.MainActivity;
import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Net;
import com.fcs.fcspos.model.Vehicle;



public class PositionActivity extends AppCompatActivity {


    private Button btnSales,btnBasket,btnRecord,btnTurn,btnCalibrate;
    private Dispenser dispenser;
    private AppMfcProtocol appMfcProtocol;
    private Net net;
    private Vehicle vehicle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        appMfcProtocol = (AppMfcProtocol) getIntent().getSerializableExtra("AppMfcProtocol");
        net = (Net)getIntent().getSerializableExtra("net");
        dispenser = (Dispenser)getIntent().getSerializableExtra("dispenser");
        vehicle = new Vehicle();
        supplierStatus();
        initView();
        eventsViews();
    }


    private void supplierStatus(){
        appMfcProtocol.machineCommunication(false);
        SystemClock.sleep(80);
        if(appMfcProtocol.getEstado()==dispenser.getCod_LISTO()){
            Toast.makeText(getApplicationContext(), "Manguera levantada", Toast.LENGTH_SHORT).show();
        }else if(appMfcProtocol.getEstado()==dispenser.getCod_SURTIENDO()){
            Toast.makeText(getApplicationContext(), "Surtiendo", Toast.LENGTH_SHORT).show();
            pendingSale(dispenser.getCod_SURTIENDO());
        }else if(appMfcProtocol.getEstado()==dispenser.getCod_VENTA()){
            pendingSale(dispenser.getCod_VENTA());
        }else if(appMfcProtocol.getEstado()==dispenser.getCod_ERROR()){
            Toast.makeText(getApplicationContext(), "No hay respuesta de la MFC", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void pendingSale(int currentProcess){
        final int TYPE_VEHICLE=0;
        final SharedPreferences sharedPref = PositionActivity.this.getSharedPreferences("pendingSales", MODE_PRIVATE);
        String s = sharedPref.getString(net.getSsid() + "/" + appMfcProtocol.getProgramming().getPosition(), null);
        if(s!=null){
            String[] splitAnswer = s.split("/");
            vehicle.setKind(Integer.parseInt(splitAnswer[TYPE_VEHICLE]));
            validateActiveSale((byte) currentProcess, true);
        }else {
            validateActiveSale((byte) currentProcess, false);
        }
    }

    private void validateActiveSale(byte currentProcess, boolean pendingSale){
        if(pendingSale){
            nextActivity(currentProcess);
        }else{
            Toast.makeText(getApplicationContext(),"Esta venta no le pertenece a este dispositivo", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void nextActivity(byte currentProcess){
        Intent i = new Intent(getApplicationContext(), SalesActivity.class);
        i.putExtra("surtidor",dispenser);
        i.putExtra("currentProcess", currentProcess);
        i.putExtra("appMfcProtocol", appMfcProtocol);
        i.putExtra("net", net);
        i.putExtra("vehicle", vehicle);
        startActivity(i);
    }

    private void eventsViews() {
        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity(dispenser.getCod_ESPERA());
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
