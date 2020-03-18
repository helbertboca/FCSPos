package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.SaleOption;

/**
 * A simple {@link Fragment} subclass.
 */
public class VolumeFragment extends Fragment {


    private SaleOption saleOption;
    private AppMfcProtocol appMfcProtocol;
    private Client client;
    private EditText edtVolume;


    public VolumeFragment(AppMfcProtocol appMfcProtocol, Client client) {
        this.appMfcProtocol = appMfcProtocol;
        this.client = client;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_volume, container, false);
        edtVolume = view.findViewById(R.id.edtVolume);
        Button btnAcceptVol = view.findViewById(R.id.btnAcceptVol);
        btnAcceptVol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationVolumeXcustomer();
            }
        });
        return view;
    }


    private void validationVolumeXcustomer(){
        final String SALEKIND_COUNTED="Counted", SALEKIND_CREDIT="Credit";

        if(!edtVolume.getText().toString().equals("")){
            double volume = Double.parseDouble( edtVolume.getText().toString().replace(",",".") );
            if(appMfcProtocol.getProgramming().getKind().equals(SALEKIND_COUNTED)){
                saleOption.volume(volume);
            }else{
                if(volume <= client.getAvailableVolume()){
                    saleOption.volume(volume);
                }else{
                    Toast.makeText(getContext(), "El valor excede al cupo del que dispone", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(getContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }

}
