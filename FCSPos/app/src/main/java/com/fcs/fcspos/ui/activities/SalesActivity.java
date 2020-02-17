package com.fcs.fcspos.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.MfcWifi;
import com.fcs.fcspos.model.Sale;
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
    private Sale sale;
    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        sale = new Sale();
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
                sale.setKind("Counted");
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, productKindFragment).
                        addToBackStack(null).commit();
                break;
            case LOYAL:
                sale.setKind("Loyal");
                break;
            case CREDIT:
                sale.setKind("Credit");
                break;
            case WAY_TO_PAY:
                sale.setKind("Way To Pay");
                break;
        }
    }

    @Override
    public void optionProductKind(int selectedProduct) {
        final int PRODUCT_ONE=1, PRODUCT_TWO=2;

        switch (selectedProduct){
            case PRODUCT_ONE:
                sale.setProduct(PRODUCT_ONE);
                break;
            case PRODUCT_TWO:
                sale.setProduct(PRODUCT_TWO);
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
        sale.setVehicle(vehicle);
        fragmentManager.beginTransaction().replace(R.id.contSaleKind, presetKindFragment).
                addToBackStack(null).commit();
    }

    @Override
    public void optionPresetKind(int selectedKindPreset) {
        final int MONEY=1,VOLUME=2,FULL=3;
        switch (selectedKindPreset){
            case MONEY:
                sale.setPresetKind(MONEY);
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, moneyFragment).
                        addToBackStack(null).commit();
            break;
            case VOLUME:
                sale.setPresetKind(VOLUME);
                fragmentManager.beginTransaction().replace(R.id.contSaleKind, volumeFragment).
                        addToBackStack(null).commit();
                break;
            case FULL:
                //mostrar levante manguera
                sale.setPresetKind(FULL);
                break;
        }
    }

    @Override
    public void money(int money) {
        sale.setMoney(money);
        sale.setVoleme(0);
        //hacer calculo para volumen
        System.out.println(sale.toString());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sale.getPresetKind());
        stringBuilder.append(sale.getProduct());
        stringBuilder.append(sale.getMoney());

        MfcWifi mfcWifi = MfcWifi.getInstance("ESP32", "123456789", "192.168.4.1", 80);
        //MfcWifi mfcWifi = MfcWifi.getInstance("FCS_INVITADOS", "Fcs.inv*!!", "192.168.102.29", 8080);
        mfcWifi.sendRequest(stringBuilder.toString());

        System.out.println("haodf");
    }

    @Override
    public void volume(double volume) {
        sale.setVoleme(volume);
        sale.setMoney(0);
        //hacer calculo para dinero
        System.out.println(sale.toString());
        System.out.println(sale.getVehicle().toString());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack();
    }

}
