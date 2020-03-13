package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        edtMileageVehicle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if( (keyEvent.getAction() == KeyEvent.ACTION_DOWN) || (i == KeyEvent.KEYCODE_ENTER)){
                    saleOption.mileage(edtMileageVehicle.getText().toString());
                }
                return false;
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
