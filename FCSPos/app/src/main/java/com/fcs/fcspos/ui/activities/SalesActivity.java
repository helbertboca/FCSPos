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
import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Identification;
import com.fcs.fcspos.model.Net;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Sale;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.model.Station;
import com.fcs.fcspos.model.Vehicle;
import com.fcs.fcspos.ui.fragments.AuthorizedSupplyFragment;
import com.fcs.fcspos.ui.fragments.FillingUpFragment;
import com.fcs.fcspos.ui.fragments.IdentificacionMethodFragment;
import com.fcs.fcspos.ui.fragments.MileageFragment;
import com.fcs.fcspos.ui.fragments.MoneyFragment;
import com.fcs.fcspos.ui.fragments.PresetKindFragment;
import com.fcs.fcspos.ui.fragments.ProductKindFragment;
import com.fcs.fcspos.ui.fragments.ReceiptFragment;
import com.fcs.fcspos.ui.fragments.SaleDataFragment;
import com.fcs.fcspos.ui.fragments.SalesKindFragment;
import com.fcs.fcspos.ui.fragments.UpHoseFragment;
import com.fcs.fcspos.ui.fragments.ValidatingFragment;
import com.fcs.fcspos.ui.fragments.VehicleKindFragment;
import com.fcs.fcspos.ui.fragments.VolumeFragment;
import com.google.gson.Gson;

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
    private final String SALEKIND_COUNTED="Counted", SALEKIND_CREDIT="Credit";
    private Client client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        dispenser =(Dispenser)getIntent().getSerializableExtra("surtidor");
        currentProcess = (byte)getIntent().getSerializableExtra("currentProcess");
        AppMfcProtocol appMfcProtocol = (AppMfcProtocol)getIntent().getSerializableExtra("appMfcProtocol");//v1
        net = (Net)getIntent().getSerializableExtra("net");
        station = (Station) getIntent().getSerializableExtra("station");
        programming = appMfcProtocol.getProgramming();
        vehiclePending = appMfcProtocol.getProgramming().getVehicle();
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
                programming.setKind(SALEKIND_COUNTED); //v1
                break;
            case LOYAL:
                programming.setKind("Loyal"); //v1
                break;
            case CREDIT:
                programming.setKind(SALEKIND_CREDIT); //v1
                break;
            case WAY_TO_PAY:
                programming.setKind("Way To Pay");//v1
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
                programming.setProduct(PRODUCT_ONE);//v1
                break;
            case PRODUCT_TWO:
                programming.setProduct(PRODUCT_TWO);//v1
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
        programming.setVehicle(vehiclePending);//v1

        if(programming.getKind().equals(SALEKIND_COUNTED)){//v1
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, new PresetKindFragment()).
                    addToBackStack(null).commit();
        }else if(programming.getKind().equals(SALEKIND_CREDIT)){//v1
            fragmentManager.beginTransaction().replace(R.id.contSaleKind,
                    new IdentificacionMethodFragment()).addToBackStack(null).commit();// identification method
        }
    }

    @Override
    public void optionPresetKind(int selectedKindPreset) {
        final int FULL=3,MONEY=2,VOLUME=1;
        switch (selectedKindPreset){
            case MONEY:
                programming.setPresetKind(MONEY); //V1
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, new MoneyFragment()).
                        addToBackStack(null).commit();
            break;
            case VOLUME:
                programming.setPresetKind(VOLUME); //V1
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, new VolumeFragment()).
                        addToBackStack(null).commit();
                break;
            case FULL:
                if( dispenser.getNumberOfDigits()>=7){
                    programming.setQuantity(9999900); //v1
                }else{
                    programming.setQuantity(999900);//v1
                }
                programming.setPresetKind(FULL);//v1
                sendShuduledSale();
                break;
        }
    }


    @Override
    public void identificationKind(Identification identification) {
        switch (identification.getName()){
            case "LicensePlate":
                programming.setIdentification(identification);//v1
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, new MileageFragment()).
                        addToBackStack(null).commit();
                break;
            case "Ibutton":
                break;
            case "Rfid":
                break;
            case "Rings":
                break;
            case "Covenants":
                break;
        }
    }

    @Override
    public void mileage(String value) {
        programming.getIdentification().setValue(value);//v1
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, new ValidatingFragment(programming)).//v1
                addToBackStack(null).commit();
    }

    @Override
    public void authorizedCustomer(Client client) {
        if(client!=null){
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, new AuthorizedSupplyFragment(client)).
                    addToBackStack(null).commit();
        }else{
            Toast.makeText(getApplicationContext(),"El cliente no pudo ser validado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showCustomerInformation(Client client){
        this.client = client;
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, new PresetKindFragment()).
                addToBackStack(null).commit();
    }


    @Override
    public void money(int money) {
        programming.setQuantity(money); //v1
        sendShuduledSale();
    }


    @Override
    public void volume(double volume) {
        int volumeInt = ((int)(volume*100))*10;
        programming.setPresetKind(1);//v1
        programming.setQuantity(volumeInt);//v1
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
        final SharedPreferences.Editor editor = sharedPref.edit();//para venta de credito necesito, el cliente y la programacion(vechiuclo , preset)
        Gson gson = new Gson();
        //String jsonVehiclePend = gson.toJson(vehiclePending);
        String jsonVehiclePend = gson.toJson(vehiclePending);
        switch (action){
            case SAVE:
                editor.putString(net.getSsid() + "/" + programming.getPosition(), jsonVehiclePend);
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

    private void endSaleCredit(Sale sale){
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
            AppMfcProtocol appMfcProtocol = new AppMfcProtocol(MfcWifiCom.getInstance(net.getIp(), net.getPort()), dispenser);//abro conexion
            appMfcProtocol.setProgramming(programming);//envio programacion del usuario

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
            //if(programming.getKind().equals(SALEKIND_COUNTED)){
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, new SaleDataFragment(appMfcProtocol)).
                        addToBackStack(null).commit();//comportamiento extraño
            /*}else{//es credito
                for (byte i=0; i<6;i++){
                    if(appMfcProtocol.getSale()!=null){
                        Sale sale = appMfcProtocol.getSale();
                        sale.setClient(client);
                        sale.setVehicle(programming.getVehicle());
                        endSaleCredit(sale);
                    }else {
                        System.out.println("Recogiendo venta credito...");
                    }
                }
            }*/
        }


        private void killThread(boolean kill){
            this.kill = kill;
        }

    }
    //----------------------------------------------------------------------------------------------


}
