package com.fcs.fcspos.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfc;
import com.fcs.fcspos.io.MfcWifi;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.model.Vehicle;
import com.fcs.fcspos.ui.fragments.FillingUpFragment;
import com.fcs.fcspos.ui.fragments.MoneyFragment;
import com.fcs.fcspos.ui.fragments.PresetKindFragment;
import com.fcs.fcspos.ui.fragments.ProductKindFragment;
import com.fcs.fcspos.ui.fragments.ReceiptFragment;
import com.fcs.fcspos.ui.fragments.SalesKindFragment;
import com.fcs.fcspos.ui.fragments.UpHoseFragment;
import com.fcs.fcspos.ui.fragments.VehicleKindFragment;
import com.fcs.fcspos.ui.fragments.VolumeFragment;



public class SalesActivity extends AppCompatActivity  implements SaleOption{


    private FragmentManager fragmentManager;
    private SalesKindFragment salesKindFragment;
    private ProductKindFragment productKindFragment;
    private VehicleKindFragment vehicleKindFragment;
    private PresetKindFragment presetKindFragment;
    private MoneyFragment moneyFragment;
    private VolumeFragment volumeFragment;
    private UpHoseFragment upHoseFragment;
    private FillingUpFragment fillingUpFragment;
    private ReceiptFragment receiptFragment;
    private Programming programming;
    private Vehicle vehicle;
    private Dispenser dispenser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        dispenser =(Dispenser)getIntent().getSerializableExtra("surtidor");
        programming = new Programming();
        vehicle = new Vehicle();
        addFragmentos();
    }


    private void addFragmentos() {
        fragmentManager = getSupportFragmentManager();
        instantiateFragmets();
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, salesKindFragment).commit();
    }

    private void instantiateFragmets() {
        salesKindFragment = new SalesKindFragment();
        productKindFragment = new ProductKindFragment();
        vehicleKindFragment = new VehicleKindFragment();
        presetKindFragment = new PresetKindFragment();
        moneyFragment = new MoneyFragment();
        volumeFragment = new VolumeFragment();
        upHoseFragment = new UpHoseFragment();
        fillingUpFragment = new FillingUpFragment();
        receiptFragment = new ReceiptFragment();
    }

    @Override
    public void optionSaleKind(int selectedOption) {
        final int COUNTED=1,LOYAL=2,CREDIT=3,WAY_TO_PAY=4;

        programming.setPosition((byte)1);//dato quemado para posicion uno ya que no se ha realizado
        //la previa pantalla

        switch (selectedOption){
            case COUNTED:
                programming.setKind("Counted");
                break;
            case LOYAL:
                programming.setKind("Loyal");
                break;
            case CREDIT:
                programming.setKind("Credit");
                break;
            case WAY_TO_PAY:
                programming.setKind("Way To Pay");
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, productKindFragment).
                addToBackStack(null).commit();
    }

    @Override
    public void optionProductKind(int selectedProduct) {
        final int PRODUCT_ONE=1, PRODUCT_TWO=2;

        switch (selectedProduct){
            case PRODUCT_ONE:
                programming.setProduct(PRODUCT_ONE);
                break;
            case PRODUCT_TWO:
                programming.setProduct(PRODUCT_TWO);
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, vehicleKindFragment).
                addToBackStack(null).commit();
    }

    @Override
    public void optionVehicleKind(int selectedVehicle) {
        final int PESADO=1, PARTICULAR=2, TAXI=3, MOTO=4, OTRO=5;
        switch (selectedVehicle){
            case PESADO:
                vehicle.setKind(PESADO);
                break;
            case PARTICULAR:
                vehicle.setKind(PARTICULAR);
                break;
            case TAXI:
                vehicle.setKind(TAXI);
                break;
            case MOTO:
                vehicle.setKind(MOTO);
                break;
            case OTRO:
                vehicle.setKind(OTRO);
                break;
        }
        programming.setVehicle(vehicle);
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, presetKindFragment).
                addToBackStack(null).commit();
    }

    @Override
    public void optionPresetKind(int selectedKindPreset) {
        final int FULL=3,MONEY=2,VOLUME=1;
        switch (selectedKindPreset){
            case MONEY:
                programming.setPresetKind(MONEY);
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, moneyFragment).
                        addToBackStack(null).commit();
            break;
            case VOLUME:
                programming.setPresetKind(VOLUME);
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, volumeFragment).
                        addToBackStack(null).commit();
                break;
            case FULL:
                if( dispenser.getNumberOfDigits()>=7){
                    programming.setQuantity(9999900);
                }else{
                    programming.setQuantity(999900);
                }
                programming.setPresetKind(FULL);
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, upHoseFragment).
                        addToBackStack(null).commit();//Levante la manguera
                sendShuduledSale();
                break;
        }
    }


    @Override
    public void money(int money) {
        programming.setQuantity(money);
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, upHoseFragment).
                addToBackStack(null).commit();//Levante la manguera
        sendShuduledSale();
    }


    @Override
    public void volume(double volume) {
        int volumeInt = ((int)(volume*100))*10;
        programming.setPresetKind(1);
        programming.setQuantity(volumeInt);
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, upHoseFragment).
                addToBackStack(null).commit();//Levante la manguera
        sendShuduledSale();
    }

    @Override
    public void receipt(short cantidad) {
        if(cantidad>1){
            finish();
        }else{
            finish();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack();
    }



    private void sendShuduledSale(){
        PrimeThread p = new PrimeThread(143);
        p.start();
    }


    class PrimeThread extends Thread {
        long minPrime;
        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            int ERROR=0, ESPERA=6, LISTO=7, AUTORIZADO=8, SURTIENDO=9, VENTA=10;

            MfcWifi mfcWifi = MfcWifi.getInstance("ESP32", "123456789", "192.168.4.1", 80);
            //MfcWifi mfcWifi = MfcWifi.getInstance("FCS_INVITADOS", "Fcs.inv*!!", "192.168.102.29", 8080);
            AppMfc appMfc = new AppMfc(mfcWifi);//abro conexion
            appMfc.setProgramming(programming);//envio programacion del usuario
            do {
                appMfc.machineCommunication();
            } while (appMfc.getEstado() != LISTO);
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, fillingUpFragment).
                    addToBackStack(null).commit();//Levante la manguera
            do {
                appMfc.machineCommunication();
            } while (appMfc.getEstado() != VENTA);
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, receiptFragment).
                    addToBackStack(null).commit();//Levante la manguera
            System.out.println(appMfc.getSale());
        }
    }


}
