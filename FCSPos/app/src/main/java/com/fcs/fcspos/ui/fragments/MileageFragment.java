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
import com.fcs.fcspos.model.SaleOption;

/**
 * A simple {@link Fragment} subclass.
 */
public class MileageFragment extends Fragment {


    private SaleOption saleOption;


    public MileageFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_mileage, container, false);
        final EditText edtMileageVehicle = view.findViewById(R.id.edtKilometrajeVehiculo);

        Button btnOkKilometraje =view.findViewById(R.id.btnOkKilometraje);
        btnOkKilometraje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.mileage(edtMileageVehicle.getText().toString());
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
