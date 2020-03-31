package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.model.SaleOption;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpHoseFragment extends Fragment {

    private SaleOption saleOption;
    private AppMfcProtocol appMfcProtocol;


    public UpHoseFragment(AppMfcProtocol appMfcProtocol){
        this.appMfcProtocol = appMfcProtocol;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HoseThread hoseThread = new HoseThread(27);
        hoseThread.start();
        return inflater.inflate(R.layout.fragment_up_hose, container, false);
    }

    //----------------------------------------------------------------------------------------------
    class HoseThread extends Thread {

        private long minPrime;
        private boolean correctHose=false;

        HoseThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            short count=0;
            do {
                appMfcProtocol.machineCommunication(true);
                if(appMfcProtocol.isCorrectHose()){
                    correctHose=true;
                    break;
                }
                count++;
                if(count>minPrime){
                    correctHose=false;
                    break;
                }
            } while (appMfcProtocol.getEstado() == appMfcProtocol.getDispenser().getCod_LISTO() ||
                    appMfcProtocol.getEstado() == appMfcProtocol.getDispenser().getCod_ESPERA());
            saleOption.correctHose(correctHose);
        }

    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }


}
