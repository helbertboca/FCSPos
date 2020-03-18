package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.SaleOption;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorizedSupplyFragment extends Fragment {

    private Client client;
    private SaleOption saleOption;
    private AppMfcProtocol appMfcProtocol;


    public AuthorizedSupplyFragment(Client client, AppMfcProtocol appMfcProtocol) {
        this.client = client;
        this.appMfcProtocol = appMfcProtocol;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_authorized_supply, container, false);
        if( !(appMfcProtocol.getProgramming().getProduct() == client.getAuthorizedProduct()) ){
            client.setMessage("El vehiculo no esta autorizado para este producto");
            TextView txtSuministroAutorizado = view.findViewById(R.id.txtSuministroAutorizado);
            txtSuministroAutorizado.setVisibility(View.INVISIBLE);
            saleOption.showCustomerInformation(null);
        }else{
            TextView  txtInfoClient =view.findViewById(R.id.txtInfoCliente);
            txtInfoClient.setText(client.getMessage());
            new PrimeThread(4).start();
        }
        return view;
    }


    //----------------------------------------------------------------------------------------------
    class PrimeThread extends Thread {
        long minPrime;

        private PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            byte count=0;
            while (minPrime>count){
                SystemClock.sleep(1000);
                count++;
            }
            prueba();
        }
    }
    //----------------------------------------------------------------------------------------------


    public void prueba(){
        saleOption.showCustomerInformation(client);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }



}
