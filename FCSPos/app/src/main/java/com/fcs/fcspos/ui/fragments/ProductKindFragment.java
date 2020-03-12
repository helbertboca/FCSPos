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
public class ProductKindFragment extends Fragment {

    private SaleOption saleOption;
    private final int PRODUCT_ONE=1, PRODUCT_TWO=2;

    public ProductKindFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_kind, container, false);

        LinearLayout llFirstProduct = view.findViewById(R.id.llFirstProduct);
        LinearLayout llSecondProduct = view.findViewById(R.id.llSecondProduct);
        llFirstProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionProductKind(PRODUCT_ONE);
            }
        });
        llSecondProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionProductKind(PRODUCT_TWO);
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
