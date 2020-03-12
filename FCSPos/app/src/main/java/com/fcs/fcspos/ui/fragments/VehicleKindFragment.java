package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

        LinearLayout llHeavy = view.findViewById(R.id.llHeavy);
        LinearLayout llParticular = view.findViewById(R.id.llParticular);
        LinearLayout llCab = view.findViewById(R.id.llCab);
        LinearLayout llMotorcycle = view.findViewById(R.id.llMotorcycle);
        LinearLayout llOther = view.findViewById(R.id.llOther);
        llHeavy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionVehicleKind(PESADO);
            }
        });
        llParticular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionVehicleKind(PARTICULAR);
            }
        });
        llCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionVehicleKind(TAXI);
            }
        });
        llMotorcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionVehicleKind(MOTO);
            }
        });
        llOther.setOnClickListener(new View.OnClickListener() {
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
