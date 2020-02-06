package com.fcs.fcspos.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.fragments.Calibrar;
import com.fcs.fcspos.fragments.CanastaContado;
import com.fcs.fcspos.fragments.CanastaCredito;
import com.fcs.fcspos.fragments.Configurar;
import com.fcs.fcspos.fragments.Consignar;
import com.fcs.fcspos.fragments.Imprimir;
import com.fcs.fcspos.fragments.Turno;
import com.fcs.fcspos.fragments.VentasContado;
import com.fcs.fcspos.fragments.VentasCredito;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String BASE_URL = "http://fcservices.distracom.com.co/TestRestPos/TramaRestService.svc/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);

        setFragmentByDeafult();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                boolean fragmentTransaction = false;
                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.menu_calibrar:
                        fragment = new Calibrar();
                        fragmentTransaction = true;
                        break;

                    case R.id.menu_canasta_contado:
                        fragment = new CanastaContado();
                        fragmentTransaction = true;
                        break;

                    case R.id.menu_canasta_credito:
                        fragment = new CanastaCredito();
                        fragmentTransaction = true;
                        break;

                    case R.id.menu_configurar:
                        fragment = new Configurar();
                        fragmentTransaction = true;
                        break;

                    case R.id.menu_consignar:
                        fragment = new Consignar();
                        fragmentTransaction = true;
                        break;

                    case R.id.menu_imprimir:
                        fragment = new Imprimir();
                        fragmentTransaction = true;
                        break;

                    case R.id.menu_turno:
                        fragment = new Turno();
                        fragmentTransaction = true;
                        break;

                    case R.id.menu_venta_contado:
                        fragment = new VentasContado();
                        fragmentTransaction = true;
                        break;

                    case R.id.menu_venta_credito:
                        fragment = new VentasCredito();
                        fragmentTransaction = true;
                        break;
                }

                if (fragmentTransaction){
                    chanceFragment(fragment, menuItem);
                    drawerLayout.closeDrawers();
                }

                return true;
            }
        });

    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFragmentByDeafult(){
        chanceFragment(new VentasContado(), navigationView.getMenu().getItem(0));
    }

    private void chanceFragment(Fragment fragment, MenuItem menuItem){
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.content_frame, fragment).commit();
        menuItem.setChecked(true);
        getSupportActionBar().setTitle(menuItem.getTitle());
    }

    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case android.R.id.home:
                //abrir menu lateral
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
