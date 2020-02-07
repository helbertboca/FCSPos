package com.fcs.fcspos.ui.activities;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.OpcionMenu;


public class MainActivity extends AppCompatActivity implements OpcionMenu {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String BASE_URL = "http://fcservices.distracom.com.co/TestRestPos/TramaRestService.svc/";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MenuPrincipal menuPrincipal = new MenuPrincipal();

    }

    @Override
    public void opcionMenuElegida(int opcionSeleccionada) {
        System.out.println("presionado");
    }



}
