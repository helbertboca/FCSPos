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
public class SalesKindFragment extends Fragment {


    private SaleOption saleOption;
    private final int COUNTED=1,LOYAL=2,CREDIT=3,WAY_TO_PAY=4;

    public SalesKindFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sales_kind, container, false);
        Button btnCounted = view.findViewById(R.id.btnCounted);
        Button btnLoyal = view.findViewById(R.id.btnLoyal);
        Button btnCredit = view.findViewById(R.id.btnCredit);
        Button btnWayToPay = view.findViewById(R.id.btnWayToPay);
        btnCounted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionSaleKind(COUNTED);
            }
        });
        btnLoyal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionSaleKind(LOYAL);
            }
        });
        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionSaleKind(CREDIT);
            }
        });
        btnWayToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionSaleKind(WAY_TO_PAY);
            }
        });
        return view;
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }

}
