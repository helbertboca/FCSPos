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
public class SalesKindFragment extends Fragment {


    private SaleOption saleOption;
    private final int COUNTED=1,LOYAL=2,CREDIT=3,WAY_TO_PAY=4;

    public SalesKindFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sales_kind, container, false);

        LinearLayout llCounted = view.findViewById(R.id.llCounted);
        LinearLayout llLoyal = view.findViewById(R.id.llLoyal);
        LinearLayout llCredit = view.findViewById(R.id.llCredit);
        LinearLayout llWayToPay = view.findViewById(R.id.llWayToPay);
        llCounted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionSaleKind(COUNTED);
            }
        });
        llLoyal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionSaleKind(LOYAL);
            }
        });
        llCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionSaleKind(CREDIT);
            }
        });
        llWayToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionSaleKind(WAY_TO_PAY);
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
