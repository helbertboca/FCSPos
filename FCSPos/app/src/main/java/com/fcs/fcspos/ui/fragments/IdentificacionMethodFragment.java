package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.Identification;
import com.fcs.fcspos.model.SaleOption;

/**
 * A simple {@link Fragment} subclass.
 */
public class IdentificacionMethodFragment extends Fragment {


    private SaleOption saleOption;

    public IdentificacionMethodFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_identificacion_method, container, false);
        final EditText edtLicensePlate = view.findViewById(R.id.edtPlaca);
        Button btnOkPlaca =view.findViewById(R.id.btnOkPlaca);
        Button btnIbutton = view.findViewById(R.id.btnIbutton);
        Button btnRfid = view.findViewById(R.id.btnRfid);
        Button btnRings = view.findViewById(R.id.btnAnillos);
        Button btnCovenants =view.findViewById(R.id.btnConvenios);
        final Identification identification = new Identification();

        btnOkPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identification.setName("LicensePlate");
                identification.setValue(edtLicensePlate.getText().toString());
                saleOption.identificationKind(identification);
            }
        });
        btnIbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identification.setName("Ibutton");
                saleOption.identificationKind(identification);
            }
        });
        btnRfid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identification.setName("Rfid");
                saleOption.identificationKind(identification);
            }
        });
        btnRings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identification.setName("Rings");
                saleOption.identificationKind(identification);
            }
        });
        btnCovenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identification.setName("Covenants");
                saleOption.identificationKind(identification);
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
