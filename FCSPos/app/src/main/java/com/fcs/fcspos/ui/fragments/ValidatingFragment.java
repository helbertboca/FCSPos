package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fcs.fcspos.R;

import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.SaleOption;


/**
 * A simple {@link Fragment} subclass.
 */
public class ValidatingFragment extends Fragment {


    private Programming programming;
    private ProgressBar pbValidandoInfo;
    private SaleOption saleOption;
    private Client client;


    public ValidatingFragment(Programming programming) {
        this.programming = programming;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validating, container, false);
        pbValidandoInfo = view.findViewById(R.id.pbValidandoInfo);
        new DataClientThread().execute(programming);
        return view;
    }


    //----------------------------------------------------------------------------------------------
    private class DataClientThread extends AsyncTask<Programming, Void, Boolean> {

        protected void onPreExecute(){
            pbValidandoInfo.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Programming ... programmings){
            //AQUI SE ENVIA SOLICITUD Del vehiculo , SI ESTA AUTORIZADO Y CUPO
            //programmings[0].getIdentification();
            int count=0;
            while ( 5>count){
                System.out.println("Esperando respuesta de la informacion del  cliente credito que se solicita..." + count);
                SystemClock.sleep(200);
                count++;
                //if(se obtienen datos del cliente credito){
                //    return true;
                //}
            }
            client = new Client();
            client.setMessage("Cliente de AXA Seguros Colpatria vehiculos" +
                    ", cupo disponible de $15.000 con ppu de $8200 ,extra");
            client.setAuthorizedProduct((byte) 1);
            client.setAuthorizedPpu((short) 8200);
            client.setName("Julio Cardenas De los Valdez");
            client.setAvailableMoney(15000);
            client.setAvailableVolume(1.82);
            client.setAvailableFull(14924);//GENERAR funciones
            return true;
        }

        @Override
        protected void onPostExecute(Boolean respuesta){
            pbValidandoInfo.setVisibility(View.GONE);
            if(respuesta){
                saleOption.authorizedCustomer(client);
            }else{
                saleOption.authorizedCustomer(null);
            }
        }
    }
    //----------------------------------------------------------------------------------------------


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }
}
