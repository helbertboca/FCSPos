package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.SaleOption;

/**
 * A simple {@link Fragment} subclass.
 */
public class VolumeFragment extends Fragment {


    private SaleOption saleOption;

    public VolumeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_volume, container, false);
        final EditText edtVolume = view.findViewById(R.id.edtVolume);
        Button btnAcceptVol = view.findViewById(R.id.btnAcceptVol);
        btnAcceptVol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtVolume.getText().toString().equals("")){
                    saleOption.volume(Double.parseDouble(edtVolume.getText().toString()));
                }else {
                    Toast.makeText(getContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }

}
