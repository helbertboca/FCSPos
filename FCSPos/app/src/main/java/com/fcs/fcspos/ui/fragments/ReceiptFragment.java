package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.SaleOption;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptFragment extends Fragment {


    private SaleOption saleOption;

    public ReceiptFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        Button btn1Receipt = view.findViewById(R.id.btn1Recibo);
        Button btn2Receipts = view.findViewById(R.id.btn2Recibo);
        btn1Receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.receipt((short) 1);
            }
        });
        btn2Receipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.receipt((short) 2);
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
