package com.fcs.fcspos.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
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
    private final byte ERROR=0, ESPERA=6, LISTO=7, AUTORIZADO=8, SURTIENDO=9, VENTA=10;
    private byte currentProcess;
    private PrimeThread primeThread;
    private boolean scheduledSaleFlag=false;
    private Net net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        dispenser =(Dispenser)getIntent().getSerializableExtra("surtidor");
        currentProcess = (byte)getIntent().getSerializableExtra("currentProcess");
        AppMfcProtocol appMfcProtocol = (AppMfcProtocol)getIntent().getSerializableExtra("appMfcProtocol");
        net = (Net)getIntent().getSerializableExtra("net");
        programming = appMfcProtocol.getProgramming();
        vehicle = new Vehicle();
        fragmentManager = getSupportFragmentManager();
        instantiateFragmets();
        if(currentProcess!=ESPERA){
            secondThread();
        }else {
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, salesKindFragment).commit();
        }
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

    @Override
    public void positionChange(){
        startApp();
    }

    @Override
    public void endSale(Sale sale) {
        TextView textView = findViewById(R.id.infoVenta);
        textView.setText("Venta;" + sale + ", VEHICULO; " + sale.getVehicle() + ", CLIENTE; " + sale.getClient());
        System.out.println(sale + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(sale.getVehicle() + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(sale.getClient() + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, receiptFragment).commit();
    }

    @Override
    public void receipt(short cantidad) {
        if(cantidad>1){
            startApp();
        }else{
            startApp();
        }
    }

    private void startApp(){
        if(primeThread.isAlive()){
            primeThread.killThread(true);
        }
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


    private void sendShuduledSale(){
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, upHoseFragment).
                addToBackStack(null).commit();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment f: fragments) {
            getFragmentManager().popBackStack();
        }
        scheduledSaleFlag=true;
        secondThread();
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
            AppMfcProtocol appMfcProtocol = new AppMfcProtocol(mfcWifiCom);//abro conexion
            appMfcProtocol.setProgramming(programming);//envio programacion del usuario
            SaleDataFragment saleDataFragment = new SaleDataFragment(programming, appMfcProtocol);
            switch (currentProcess){
                case SURTIENDO:
                    fragmentManager.beginTransaction().replace(R.id.contSaleKind, fillingUpFragment).commit();
                    do {
                        appMfcProtocol.machineCommunication(true);
                    } while ((appMfcProtocol.getEstado() != VENTA) && (!kill));
                    break;
                case VENTA:
                    break;
                default:
                    do {
                        appMfcProtocol.machineCommunication(false);
                    } while (appMfcProtocol.getEstado() != LISTO);
                    fragmentManager.beginTransaction().replace(R.id.contSaleKind, fillingUpFragment).commit();
                    do {
                        appMfcProtocol.machineCommunication(false);
                    } while ((appMfcProtocol.getEstado() != VENTA) && (!kill));
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
