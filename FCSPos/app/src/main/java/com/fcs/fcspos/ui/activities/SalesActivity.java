package com.fcs.fcspos.ui.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fcs.fcspos.R;
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
    private FragmentTransaction fragmentTransaction;
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
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contSaleKind, salesKindFragment);
        fragmentTransaction.add(R.id.contProductKind, productKindFragment);
        fragmentTransaction.add(R.id.contVehicleKind, vehicleKindFragment);
        fragmentTransaction.add(R.id.contPresetKind, presetKindFragment);
        fragmentTransaction.add(R.id.contMoney, moneyFragment);
        fragmentTransaction.add(R.id.contVolume, volumeFragment);
        hideFragments(productKindFragment, vehicleKindFragment, presetKindFragment, moneyFragment,
                volumeFragment);
        fragmentTransaction.commit();
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
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(salesKindFragment);
                fragmentTransaction.show(productKindFragment);
                fragmentTransaction.commit();
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
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(productKindFragment);
        fragmentTransaction.show(vehicleKindFragment);
        fragmentTransaction.commit();
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
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(vehicleKindFragment);
        fragmentTransaction.show(presetKindFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void optionPresetKind(int selectedKindPreset) {
        final int MONEY=1,VOLUME=2,FULL=3;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(presetKindFragment);
        switch (selectedKindPreset){
            case MONEY:
                sale.setPresetKind(MONEY);
                fragmentTransaction.show(moneyFragment);
            break;
            case VOLUME:
                sale.setPresetKind(VOLUME);
                fragmentTransaction.show(volumeFragment);
                break;
            case FULL:
                //mostrar levante manguera
                sale.setPresetKind(FULL);
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void money(int money) {
        sale.setMoney(money);
    }

    @Override
    public void volume(double volume) {
        sale.setVoleme(volume);
    }


    private void hideFragments(Fragment... fragment) {
        for (Fragment frg: fragment) {
            fragmentTransaction.hide(frg);
        }
    }



}
