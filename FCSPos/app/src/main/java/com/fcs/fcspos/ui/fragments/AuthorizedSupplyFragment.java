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
import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.SaleOption;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorizedSupplyFragment extends Fragment {

    private Client client;
    private SaleOption saleOption;


    public AuthorizedSupplyFragment(Client client) {
        this.client = client;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_authorized_supply, container, false);
        TextView  txtInfoClient =view.findViewById(R.id.txtInfoCliente);
        txtInfoClient.setText(client.getMessage());
        PrimeThread p = new PrimeThread(5);
        p.start();
        return view;
    }

    //----------------------------------------------------------------------------------------------
    class PrimeThread extends Thread {
        long minPrime;
        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            byte count=0;
            while (minPrime<count){
                SystemClock.sleep(1000);
                count++;
            }
            saleOption.showCustomerInformation(client);
        }
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }



}
