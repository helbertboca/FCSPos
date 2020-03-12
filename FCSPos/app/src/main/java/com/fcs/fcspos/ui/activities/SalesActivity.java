package com.fcs.fcspos.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.fcs.fcspos.MainActivity;
import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.io.MfcWifiCom;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Net;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Sale;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.model.Station;
import com.fcs.fcspos.model.Vehicle;
import com.fcs.fcspos.ui.fragments.FillingUpFragment;
import com.fcs.fcspos.ui.fragments.MoneyFragment;
import com.fcs.fcspos.ui.fragments.PresetKindFragment;
import com.fcs.fcspos.ui.fragments.ProductKindFragment;
import com.fcs.fcspos.ui.fragments.ReceiptFragment;
import com.fcs.fcspos.ui.fragments.SaleDataFragment;
import com.fcs.fcspos.ui.fragments.SalesKindFragment;
import com.fcs.fcspos.ui.fragments.UpHoseFragment;
import com.fcs.fcspos.ui.fragments.VehicleKindFragment;
import com.fcs.fcspos.ui.fragments.VolumeFragment;

import java.util.List;


public class SalesActivity extends AppCompatActivity  implements SaleOption{


    private FragmentManager fragmentManager;
    private Programming programming;
    private Vehicle vehiclePending;
    private Dispenser dispenser;
    private byte currentProcess;
    private PrimeThread primeThread;
    private boolean scheduledSaleFlag=false;
    private Net net;
    private Station station;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        dispenser =(Dispenser)getIntent().getSerializableExtra("surtidor");
        currentProcess = (byte)getIntent().getSerializableExtra("currentProcess");
        AppMfcProtocol appMfcProtocol = (AppMfcProtocol)getIntent().getSerializableExtra("appMfcProtocol");
        net = (Net)getIntent().getSerializableExtra("net");
        vehiclePending = (Vehicle) getIntent().getSerializableExtra("vehicle");
        station = (Station) getIntent().getSerializableExtra("station");
        programming = appMfcProtocol.getProgramming();
        fragmentManager = getSupportFragmentManager();
        if(currentProcess!=dispenser.getCod_ESPERA()){
            secondThread();
        }else {
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, new SalesKindFragment()).commit();
        }
    }


    @Override
    public void optionSaleKind(int selectedOption) {
        final int COUNTED=1,LOYAL=2,CREDIT=3,WAY_TO_PAY=4;

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
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, new ProductKindFragment()).
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
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, new VehicleKindFragment()).
                addToBackStack(null).commit();
    }

    @Override
    public void optionVehicleKind(int selectedVehicle) {
        final int PESADO=1, PARTICULAR=2, TAXI=3, MOTO=4, OTRO=5;
        switch (selectedVehicle){
            case PESADO:
                vehiclePending.setKind(PESADO);
                break;
            case PARTICULAR:
                vehiclePending.setKind(PARTICULAR);
                break;
            case TAXI:
                vehiclePending.setKind(TAXI);
                break;
            case MOTO:
                vehiclePending.setKind(MOTO);
                break;
            case OTRO:
                vehiclePending.setKind(OTRO);
                break;
        }
        programming.setVehicle(vehiclePending);
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, new PresetKindFragment()).
                addToBackStack(null).commit();
    }

    @Override
    public void optionPresetKind(int selectedKindPreset) {
        final int FULL=3,MONEY=2,VOLUME=1;
        switch (selectedKindPreset){
            case MONEY:
                programming.setPresetKind(MONEY);
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, new MoneyFragment()).
                        addToBackStack(null).commit();
            break;
            case VOLUME:
                programming.setPresetKind(VOLUME);
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, new VolumeFragment()).
                        addToBackStack(null).commit();
                break;
            case FULL:
                if( dispenser.getNumberOfDigits()>=7){
                    programming.setQuantity(9999900);
                }else{
                    programming.setQuantity(999900);
                }
                programming.setPresetKind(FULL);
                sendShuduledSale();
                break;
        }
    }


    @Override
    public void money(int money) {
        programming.setQuantity(money);
        sendShuduledSale();
    }


    @Override
    public void volume(double volume) {
        int volumeInt = ((int)(volume*100))*10;
        programming.setPresetKind(1);
        programming.setQuantity(volumeInt);
        sendShuduledSale();
    }

    private void sendShuduledSale(){
        UpHoseFragment upHoseFragment = new UpHoseFragment(programming, net, dispenser);
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, upHoseFragment).
                addToBackStack(null).commit();
    }


    @Override
    public void correctHose(boolean is_hose) {
        if(is_hose){
            takeOutStackFragments();
            scheduledSaleFlag=true;
            secondThread();
        }else {
            System.out.println("Excedio el tiempo de levantar la manguera");
            restart();
        }
    }

    @Override
    public void positionChange(){
        takeOutStackFragments();
        startApp();
    }


    private void pendingSales_file(byte action){
        final byte SAVE=1, READ=2, DELETE=3;
        final SharedPreferences sharedPref = SalesActivity.this.getSharedPreferences("pendingSales", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        switch (action){
            case SAVE:
                editor.putString(net.getSsid() + "/" + programming.getPosition(), vehiclePending.getKind() + "/");
                editor.apply();
                break;
            case DELETE:
                editor.remove(net.getSsid() + "/" + programming.getPosition());
                editor.apply();
                break;
        }
    }


    @Override
    public void endSale(Sale sale) {
        Vehicle vehicleCurrent =sale.getVehicle();
        vehiclePending.setLicense_plate(vehicleCurrent.getLicense_plate());
        vehiclePending.setKilometres(vehicleCurrent.getKilometres());
        sale.setVehicle(vehiclePending);
        pendingSales_file((byte) 3);
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, new ReceiptFragment(sale, station, programming)).commit();
    }

    @Override
    public void receipt() {
        takeOutStackFragments();
        startApp();
    }


    private void startApp(){
        if(primeThread.isAlive()){
            primeThread.killThread(true);
        }
        restart();
    }

    private void restart(){
        takeOutStackFragments();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBackPressed() {
        if(scheduledSaleFlag){
            Toast.makeText(getApplicationContext(), "Venta ya programada", Toast.LENGTH_SHORT).show();
        }else{super.onBackPressed();
            getFragmentManager().popBackStack();
        }
    }


    private void takeOutStackFragments(){
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment f: fragments) {
            getFragmentManager().popBackStack();
        }
    }


    private void secondThread(){
        primeThread = new PrimeThread(143);
        primeThread.killThread(false);
        primeThread.start();
    }


    //----------------------------------------------------------------------------------------------
    class PrimeThread extends Thread {

        private long minPrime;
        private boolean kill;

        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            MfcWifiCom mfcWifiCom = MfcWifiCom.getInstance(net.getIp(), net.getPort());
            AppMfcProtocol appMfcProtocol = new AppMfcProtocol(mfcWifiCom, dispenser);//abro conexion
            appMfcProtocol.setProgramming(programming);//envio programacion del usuario
            SaleDataFragment saleDataFragment = new SaleDataFragment(programming, appMfcProtocol);
            FillingUpFragment fillingUpFragment = new FillingUpFragment();
            if(currentProcess == dispenser.getCod_SURTIENDO()){
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, fillingUpFragment).commit();
                scheduledSaleFlag=true;
                pendingSales_file((byte) 1);
                do {
                    appMfcProtocol.machineCommunication(true);
                } while ((appMfcProtocol.getEstado() != dispenser.getCod_VENTA()) && (!kill));
            }else if(currentProcess == dispenser.getCod_VENTA()){

            }else{
                do {
                    appMfcProtocol.machineCommunication(false);
                } while (appMfcProtocol.getEstado() != dispenser.getCod_LISTO());
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, fillingUpFragment).commit();
                scheduledSaleFlag=true;
                pendingSales_file((byte) 1);
                do {
                    appMfcProtocol.machineCommunication(false);
                } while ((appMfcProtocol.getEstado() != dispenser.getCod_VENTA()) && (!kill));
            }
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, saleDataFragment).
                    addToBackStack(null).commit();
        }


        private void killThread(boolean kill){
            this.kill = kill;
        }

    }
    //----------------------------------------------------------------------------------------------


}
