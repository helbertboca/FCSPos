package com.fcs.fcspos.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.OpcionMenu;
import com.fcs.fcspos.ui.fragments.MenuPrincipalFragment;


public class MainActivity extends AppCompatActivity implements OpcionMenu {

    private String BASE_URL = "http://fcservices.distracom.com.co/TestRestPos/TramaRestService.svc/";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MenuPrincipalFragment menuPrincipalFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuPrincipalFrag = new MenuPrincipalFragment();
        fragmentManager = getSupportFragmentManager();
        adjuntarFragmentos();
    }

    private void adjuntarFragmentos() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contMenuPrincipalFrag, menuPrincipalFrag);


        fragmentTransaction.show(menuPrincipalFrag);
        fragmentTransaction.commit();
    }

    @Override
    public void opcionMenuElegida(int opcionSeleccionada) {
        System.out.println("presionado");
    }



}
