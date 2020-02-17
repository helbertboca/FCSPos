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
        //final MfcWifi mfcWifi = MfcWifi.getInstance("FCS_INVITADOS", "Fcs.inv*!||!", "192.168.102.29", 8080);
        if(mfcWifi.conectionMfcWifi(getApplicationContext())){
            //Toast.makeText(this, "Conexion con MFC exitosa",
                    //Toast.LENGTH_SHORT).show();
            initialSettingsDispenser();
            initView();
            eventsViews();
        }

    }


    private void initialSettingsDispenser() {
        dispenser = new Dispenser();
        Side sideA = new Side();
        Side sideB = new Side();

        ArrayList<Hose> hosesLA = new ArrayList<>();
        ArrayList<Hose> hosesLB = new ArrayList<>();
        hosesLA.add(new Hose());
        hosesLA.add(new Hose());
        hosesLB.add(new Hose());
        hosesLB.add(new Hose());

        sideA.setHoses(hosesLA);
        sideB.setHoses(hosesLB);

        ArrayList<Side> sides = new ArrayList<>();
        sides.add(sideA);
        sides.add(sideB);
        dispenser.setSides(sides);
    }


    private void eventsViews() {
        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //--------------------------------------------------------------------------------------
                mfcWifi.sendRequest("estado;1");
                SystemClock.sleep(60);
                System.out.println("Respuesta pos1 = " + mfcWifi.getAnswer());

                Hose hose = dispenser.getSides().get(0).getHoses().get(0);
                hose.setState((byte) 2);

                //Intent i = new Intent(getApplicationContext(), SalesActivity.class);
                //startActivity(i);
            }
        });
        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mfcWifi.sendRequest("estado;2");
                SystemClock.sleep(60);
                System.out.println("Respuesta pos2 = " + mfcWifi.getAnswer());
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
