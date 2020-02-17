package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.content.Intent;
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
import com.fcs.fcspos.ui.activities.SalesActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoneyFragment extends Fragment {


    private SaleOption saleOption;

    public MoneyFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_money, container, false);
        final EditText edtMoney = view.findViewById(R.id.edtMoney);
        Button btnAcceptMon = view.findViewById(R.id.btnAcceptMon);
        btnAcceptMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtMoney.getText().toString().equals("")){
                    saleOption.money(Integer.parseInt(edtMoney.getText().toString()));
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
