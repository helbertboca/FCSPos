package com.fcs.fcspos.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.MfcWifi;


public class MainActivity extends AppCompatActivity{


    private Button btnSales,btnBasket,btnRecord,btnTurn,btnCalibrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MfcWifi mfcWifi = MfcWifi.getInstance("FCS_INVITADOS", "Fcs.inv*!!", "192.168.102.29", 8080);
        //final MfcWifi mfcWifi = MfcWifi.getInstance("SurtidorA", "modulo001","192.168.4.1", 80);
        if(mfcWifi.conectionMfcWifi(getApplicationContext())){
            //Toast.makeText(this, "Conexion con MFC exitosa",
                    //Toast.LENGTH_SHORT).show();
            initView();
            eventsViews();
        }

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
