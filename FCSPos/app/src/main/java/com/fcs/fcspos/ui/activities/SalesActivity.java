package com.fcs.fcspos.ui.activities;

import android.graphics.drawable.TransitionDrawable;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.MfcWifi;
import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.model.Vehicle;
import com.fcs.fcspos.ui.fragments.MoneyFragment;
import com.fcs.fcspos.ui.fragments.PresetKindFragment;
import com.fcs.fcspos.ui.fragments.ProductKindFragment;
import com.fcs.fcspos.ui.fragments.SalesKindFragment;
import com.fcs.fcspos.ui.fragments.VehicleKindFragment;
import com.fcs.fcspos.ui.fragments.VolumeFragment;



public class SalesActivity extends AppCompatActivity  implements SaleOption {


    private FragmentManager fragmentManager;
    private SalesKindFragment salesKindFragment;
    private ProductKindFragment productKindFragment;
    private VehicleKindFragment vehicleKindFragment;
    private PresetKindFragment presetKindFragment;
    private MoneyFragment moneyFragment;
    private VolumeFragment volumeFragment;
    private Programming programming;
    private Vehicle vehicle;
    private Dispenser dispenser;
    private final int ERROR=0, ESPERA=6, LISTO=7, AUTORIZADO=8, SURTIENDO=9, VENTA=10;



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
    }

    @Override
    public void optionSaleKind(int selectedOption) {
        final int COUNTED=1,LOYAL=2,CREDIT=3,WAY_TO_PAY=4;
        switch (selectedOption){
            case COUNTED:
                programming.setKind("Counted");
                //dispenser.getSides().get(0).getHoses().get(0).getSale().setKind("Counted");
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
        final int MONEY=1,VOLUME=2,FULL=3;
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
                //mostrar levante manguera
                programming.setPresetKind(FULL);
                break;
        }
    }

    private MfcWifi mfcWifi;
    @Override
    public void money(int money) {
        final int OK = 1;
        programming.setMoney(money);



        mfcWifi = MfcWifi.getInstance("ESP32", "123456789", "192.168.4.1", 80);
        //MfcWifi mfcWifi = MfcWifi.getInstance("FCS_INVITADOS", "Fcs.inv*!!", "192.168.102.29", 8080);

        //new Hilo1().start();

        mfcWifi.sendRequest("estado;1");//pido estado
        if(mfcWifi.getAnswer()!=null){
            final String[] splitAnswer = mfcWifi.getAnswer().split(";");
            if(Integer.parseInt(splitAnswer[2]) == ESPERA){
                mfcWifi.sendRequest("manguera;1");//manguera
                if(mfcWifi.getAnswer()!=null){
                    final String[] splitAnswerManguera = mfcWifi.getAnswer().split(";");
                    if(Integer.parseInt(splitAnswerManguera[2]) == OK){
                        mfcWifi.sendRequest("programar;1;M1;T2;P12000"); //programo
                        SystemClock.sleep(140);
                        if(mfcWifi.getAnswer()!=null){
                            final String[] splitProgramacion = mfcWifi.getAnswer().split(";");
                            if(Integer.parseInt(splitProgramacion[2]) == OK){
                                mfcWifi.sendRequest("autorizar;1"); //autorizo
                                if(mfcWifi.getAnswer()!=null) {
                                    final String[] splitAnswerAutorizacion = mfcWifi.getAnswer().split(";");
                                    if(Integer.parseInt(splitAnswerAutorizacion[2]) == OK){

                                        mfcWifi.sendRequest("estado;1");//pido estado
                                        System.out.println("mirando datos: " + mfcWifi.getAnswer());

                                        if(mfcWifi.getAnswer()!=null) {
                                            final String[] splitAnswerEstado = mfcWifi.getAnswer().split(";");
                                            if(Integer.parseInt(splitAnswerEstado[2]) == AUTORIZADO){
                                                System.out.println("entre");
                                                //levante la manguera imagen
                                                /*do {
                                                    mfcWifi.sendRequest("estado;1");
                                                    System.out.println("estado;" + mfcWifi.getAnswer());
                                                    if (mfcWifi.getAnswer() != null) {
                                                        System.out.println("Respuesta estado: " + mfcWifi.getAnswer());
                                                        final String[] splitState = mfcWifi.getAnswer().split(";");
                                                        if(Integer.parseInt(splitAnswerEstado[2]) == SURTIENDO){
                                                            System.out.println("SURTIENDO.....");



                                                        }else {
                                                            System.out.println("No esta en estado de surtiendo");
                                                        }
                                                    }else{
                                                        System.out.println("No hubo respuesta del estado");
                                                    }
                                                } while (true);*/
                                            }else {
                                                System.out.println("estado:" + Integer.parseInt(splitAnswerEstado[2]));
                                            }
                                        }else{
                                            System.out.println("No hubo respuesta de estado");
                                        }
                                    }else{
                                        System.out.println("error en la autorizacion");
                                    }
                                }else{
                                    System.out.println("No hubo respuesta de autorizacion");
                                }
                            }else{
                                System.out.println("error en la programacion");
                            }
                        }else{
                            System.out.println("No hubo respuesta de la programacion");
                        }
                    }else {
                        System.out.println("error en la manguera");
                    }
                }else{
                    System.out.println("No hubo respuesta de la manguera");
                }
            }
        }else{
            System.out.println("No hubo respuesta del estado");
        }



        /*do {
            mfcWifi.sendRequest("estado;1");//pido estado
            if (mfcWifi.getAnswer() != null) {
                System.out.println("Respuesta estado: " + mfcWifi.getAnswer());




            }
        } while (true);*/

    }

    class Hilo1 extends Thread{

        final int OK = 1;
        public void run(){



        }

    }







    @Override
    public void volume(double volume) {
        programming.setVoleme(volume);
        programming.setMoney(0);
        //hacer calculo para dinero
        System.out.println(programming.toString());
        System.out.println(programming.getVehicle().toString());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack();
    }












}
