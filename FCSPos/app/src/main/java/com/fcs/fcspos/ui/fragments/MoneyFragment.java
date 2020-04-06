package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.model.SaleOption;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoneyFragment extends Fragment {


    private SaleOption saleOption;
    private AppMfcProtocol appMfcProtocol;
    private EditText edtMoney;


    public MoneyFragment(AppMfcProtocol appMfcProtocol) {
        this.appMfcProtocol = appMfcProtocol;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_money, container, false);
        edtMoney = view.findViewById(R.id.edtMoney);
        Button btnAcceptMon = view.findViewById(R.id.btnAcceptMon);
        btnAcceptMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationMoneyXcustomer();
            }
        });
        return view;
    }


    private void validationMoneyXcustomer(){
        final String SALEKIND_COUNTED="Counted", SALEKIND_CREDIT="Credit";

        if(!edtMoney.getText().toString().equals("")){
            if(appMfcProtocol.getProgramming().getKind().equals(SALEKIND_COUNTED)) {
                saleOption.money(Integer.parseInt(edtMoney.getText().toString()));
            }else{
                if(Integer.parseInt(edtMoney.getText().toString())<= appMfcProtocol.getClient().getAvailableMoney()){
                    saleOption.money(Integer.parseInt(edtMoney.getText().toString()));
                }else{
                    Toast.makeText(getContext(), "El valor excede al cupo del que dispone", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(getContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }

}
