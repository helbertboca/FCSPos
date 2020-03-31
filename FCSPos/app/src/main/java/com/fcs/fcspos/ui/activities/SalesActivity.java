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
import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.Identification;
import com.fcs.fcspos.model.Net;
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
    private byte currentProcess;
    private PrimeThread primeThread;
    private boolean scheduledSaleFlag=false;
    private Net net;
    private Station station;
    private final String SALEKIND_COUNTED="Counted", SALEKIND_CREDIT="Credit";
    private AppMfcProtocol appMfcProtocol;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        currentProcess = (byte)getIntent().getSerializableExtra("currentProcess");
        appMfcProtocol = (AppMfcProtocol)getIntent().getSerializableExtra("appMfcProtocol");
        net = (Net)getIntent().getSerializableExtra("net");
        station = (Station) getIntent().getSerializableExtra("station");
        fragmentManager = getSupportFragmentManager();
        if( currentProcess!=  appMfcProtocol.getDispenser().getCod_ESPERA()){//inicio
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
                appMfcProtocol.getProgramming().setKind(SALEKIND_COUNTED);
                break;
            case LOYAL:
                appMfcProtocol.getProgramming().setKind("Loyal");
                break;
            case CREDIT:
                appMfcProtocol.getProgramming().setKind(SALEKIND_CREDIT);
                break;
            case WAY_TO_PAY:
                appMfcProtocol.getProgramming().setKind("Way To Pay");
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
                appMfcProtocol.getProgramming().setProduct(PRODUCT_ONE);
                break;
            case PRODUCT_TWO:
                appMfcProtocol.getProgramming().setProduct(PRODUCT_TWO);
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
                appMfcProtocol.getProgramming().getVehicle().setKind(PESADO);
                break;
            case PARTICULAR:
                appMfcProtocol.getProgramming().getVehicle().setKind(PARTICULAR);
                break;
            case TAXI:
                appMfcProtocol.getProgramming().getVehicle().setKind(TAXI);
                break;
            case MOTO:
                appMfcProtocol.getProgramming().getVehicle().setKind(MOTO);
                break;
            case OTRO:
                appMfcProtocol.getProgramming().getVehicle().setKind(OTRO);
                break;
        }
        if(appMfcProtocol.getProgramming().getKind().equals(SALEKIND_COUNTED)){
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, new PresetKindFragment()).
                    addToBackStack(null).commit();
        }else if(appMfcProtocol.getProgramming().getKind().equals(SALEKIND_CREDIT)){
            fragmentManager.beginTransaction().replace(R.id.contSaleKind,
                    new IdentificacionMethodFragment()).addToBackStack(null).commit();
        }
    }

    @Override
    public void optionPresetKind(int selectedKindPreset) {
        final int FULL=3,MONEY=2,VOLUME=1;
        switch (selectedKindPreset){
            case MONEY:
                appMfcProtocol.getProgramming().setPresetKind(MONEY);
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, new MoneyFragment(appMfcProtocol)).
                        addToBackStack(null).commit();
                break;
            case VOLUME:
                appMfcProtocol.getProgramming().setPresetKind(VOLUME);
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, new VolumeFragment(appMfcProtocol)).
                        addToBackStack(null).commit();
                break;
            case FULL:
                if( appMfcProtocol.getDispenser().getNumberOfDigits()>=7){
                    if(appMfcProtocol.getProgramming().getKind().equals(SALEKIND_COUNTED)){
                        appMfcProtocol.getProgramming().setQuantity(9999900);
                    }else{
                        if( appMfcProtocol.getClient().getAvailableFull()>=9999900){
                            appMfcProtocol.getProgramming().setQuantity(9999900);
                        }else{
                            appMfcProtocol.getProgramming().setQuantity( appMfcProtocol.getClient().getAvailableFull());
                        }
                    }
                }else{
                    if(appMfcProtocol.getProgramming().getKind().equals(SALEKIND_COUNTED)){
                        appMfcProtocol.getProgramming().setQuantity(999900);
                    }else{
                        if( appMfcProtocol.getClient().getAvailableFull()>=999900){
                            appMfcProtocol.getProgramming().setQuantity(9999900);
                        }else{
                            appMfcProtocol.getProgramming().setQuantity( appMfcProtocol.getClient().getAvailableFull());
                        }
                    }
                }
                appMfcProtocol.getProgramming().setPresetKind(FULL);
                sendShuduledSale();
                break;
        }
    }



    @Override
    public void identificationKind(Identification identification) {
        switch (identification.getName()){
            case "LicensePlate":
                appMfcProtocol.getProgramming().setIdentification(identification);
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
    public void mileage(String quantity) {
        appMfcProtocol.getProgramming().getVehicle().setKilometres(quantity);
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, new ValidatingFragment(appMfcProtocol.getProgramming())).
                addToBackStack(null).commit();
    }

    @Override
    public void authorizedCustomer(Client client) {//aquie se creo el cliente ya viene armado
        if(client!=null){
            appMfcProtocol.setClient(client);
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, new AuthorizedSupplyFragment(appMfcProtocol)).addToBackStack(null).commit();
        }else{
            startApp();
        }
    }

    @Override
    public void showCustomerInformation(AppMfcProtocol appMfcProtocol){
        this.appMfcProtocol = appMfcProtocol;
        if(this.appMfcProtocol.getClient()!=null){
            takeOutStackFragments();
            fragmentManager.beginTransaction().replace(R.id.contSaleKind, new PresetKindFragment()).addToBackStack(null).commit();
            scheduledSaleFlag=true;
        }else{
            startApp();
        }
    }


    @Override
    public void money(int money) {
        appMfcProtocol.getProgramming().setQuantity(money);
        sendShuduledSale();
    }


    @Override
    public void volume(double volume) {
        int volumeInt = ((int)(volume*100))*10;
        appMfcProtocol.getProgramming().setPresetKind(1);
        appMfcProtocol.getProgramming().setQuantity(volumeInt);
        sendShuduledSale();
    }

    private void sendShuduledSale(){
        UpHoseFragment upHoseFragment = new UpHoseFragment( appMfcProtocol);
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
        String jsonVehiclePend = gson.toJson(appMfcProtocol.getProgramming());//
        switch (action){
            case SAVE:
                editor.putString(net.getSsid() + "/" + appMfcProtocol.getProgramming().getPosition(), jsonVehiclePend);
                editor.apply();
                break;
            case DELETE:
                editor.remove(net.getSsid() + "/" + appMfcProtocol.getProgramming().getPosition());
                editor.apply();
                break;
        }
    }


    @Override
    public void endSale(Sale sale) {
        Vehicle vehicleCurrent =sale.getVehicle();
        appMfcProtocol.getProgramming().getVehicle().setLicense_plate(vehicleCurrent.getLicense_plate());
        appMfcProtocol.getProgramming().getVehicle().setKilometres(vehicleCurrent.getKilometres());
        sale.setVehicle(appMfcProtocol.getProgramming().getVehicle());
        pendingSales_file((byte) 3);
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, new ReceiptFragment(sale, station, appMfcProtocol.getProgramming())).commit();
    }


    @Override
    public void receipt() {
        takeOutStackFragments();
        startApp();
    }


    private void startApp(){
        if(primeThread!=null){
            if(primeThread.isAlive()){
                primeThread.killThread(true);
            }
        }
        restart();
    }

    private void restart(){
        if(appMfcProtocol.getProgramming().getKind().equals(SALEKIND_COUNTED)){
            takeOutStackFragments();
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }


    private void takeOutStackFragments(){
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment f: fragments) {
            getFragmentManager().popBackStack();
        }
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


    private void secondThread(){
        primeThread = new PrimeThread();
        primeThread.killThread(false);
        primeThread.start();
    }


    //----------------------------------------------------------------------------------------------
    class PrimeThread extends Thread {


        private boolean kill;

        private PrimeThread() {
        }

        public void run() {
            FillingUpFragment fillingUpFragment = new FillingUpFragment();
            if(currentProcess == appMfcProtocol.getDispenser().getCod_SURTIENDO()){
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, fillingUpFragment).commit();
                scheduledSaleFlag=true;
                pendingSales_file((byte) 1);
                do {
                    appMfcProtocol.machineCommunication(true);
                } while ((appMfcProtocol.getEstado() !=  appMfcProtocol.getDispenser().getCod_VENTA()) && (!kill));
            }else if(currentProcess == appMfcProtocol.getDispenser().getCod_VENTA()){

            }else{
                do {
                    appMfcProtocol.machineCommunication(false);
                } while (appMfcProtocol.getEstado() != appMfcProtocol.getDispenser().getCod_LISTO());
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, fillingUpFragment).commit();
                scheduledSaleFlag=true;
                pendingSales_file((byte) 1);
                do {
                    appMfcProtocol.machineCommunication(false);
                } while ((appMfcProtocol.getEstado() != appMfcProtocol.getDispenser().getCod_VENTA()) && (!kill));
            }
            if(!kill){
                if(appMfcProtocol.getProgramming().getKind().equals(SALEKIND_COUNTED)){
                    fragmentManager.beginTransaction().replace(R.id.contSaleKind, new SaleDataFragment(appMfcProtocol)).
                            addToBackStack(null).commit();
                }else{//es credito
                    endCreditSale();
                    startApp();
                }
            }

        }


        private void endCreditSale(){
            for (byte i=0; i<6;i++){
                if(appMfcProtocol.getSale()!=null){
                    pendingSales_file((byte)3);
                    break;
                }else {
                    System.out.println("Recogiendo venta credito...");
                }
            }
            for(byte i=0; i<=3;i++){
                if(appMfcProtocol.changePrice(appMfcProtocol.getProgramming().getPosition(),
                        appMfcProtocol.getProgramming().getProduct(), appMfcProtocol.getDispenser())){
                    break;
                }else {
                    System.out.println("Cambiando a precio estandar...");
                }
            }
        }


        private void killThread(boolean kill){
            this.kill = kill;
        }

    }
    //----------------------------------------------------------------------------------------------


}
