package com.fcs.fcspos.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.Sale;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.ui.fragments.SalesKindFragment;

public class SalesActivity extends AppCompatActivity  implements SaleOption {


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SalesKindFragment salesKindFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        salesKindFragment = new SalesKindFragment();
        fragmentManager = getSupportFragmentManager();
        adjuntarFragmentos();
    }


    private void adjuntarFragmentos() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contSaleKind, salesKindFragment);
        fragmentTransaction.show(salesKindFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void optionSaleKind(int selectedOption) {
        final int COUNTED=1,LOYAL=2,CREDIT=3,WAY_TO_PAY=4;

        Sale sale = new Sale();
        switch (selectedOption){
            case COUNTED:
                sale.setKind("Counted");
                //quitar anterior fragmento
                //colocar nuevo fragmento
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



}
