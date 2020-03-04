package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.io.MfcWifiCom;
import com.fcs.fcspos.model.Dispenser;
import com.fcs.fcspos.model.Net;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.SaleOption;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpHoseFragment extends Fragment {

    private Programming programming;
    private Net net;
    private SaleOption saleOption;
    private Dispenser dispenser;


    public UpHoseFragment(Programming programming, Net net, Dispenser dispenser){
        this.programming = programming;
        this.net = net;
        this.dispenser = dispenser;
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
            MfcWifiCom mfcWifiCom = MfcWifiCom.getInstance(net.getIp(), net.getPort());
            AppMfcProtocol appMfcProtocol = new AppMfcProtocol(mfcWifiCom,dispenser);//abro conexion
            appMfcProtocol.setProgramming(programming);//envio programacion del usuario
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
            } while (appMfcProtocol.getEstado() == dispenser.getCod_LISTO() || appMfcProtocol.getEstado() == dispenser.getCod_ESPERA());
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
