package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.SaleOption;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleKindFragment extends Fragment {


    private SaleOption saleOption;
    private final int PESADO=1, PARTICULAR=2, TAXI=3, MOTO=4, OTRO=5;

    public VehicleKindFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vehicle_kind, container, false);
        Button btnHeavy = view.findViewById(R.id.btnHeavy);
        Button btnParticular = view.findViewById(R.id.btnParticular);
        Button btnCab = view.findViewById(R.id.btnCab);
        Button btnMotorcycle = view.findViewById(R.id.btnMotorcycle);
        Button btnOther = view.findViewById(R.id.btnOther);
        btnHeavy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionVehicleKind(PESADO);
            }
        });
        btnParticular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionVehicleKind(PARTICULAR);
            }
        });
        btnCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionVehicleKind(TAXI);
            }
        });
        btnMotorcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionVehicleKind(MOTO);
            }
        });
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionVehicleKind(OTRO);
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
