package com.fcs.fcspos.ui.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.Sale;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.model.Vehicle;
import com.google.android.material.textfield.TextInputEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class SaleDataFragment extends Fragment {


    private SaleOption saleOption;
    private AppMfcProtocol appMfcProtocol;
    private Vehicle vehicle;
    private Client client;
    private TextInputEditText edtLicensePlate, edtIdentificationCard, edtNit, edtMileage, edtProgrammedValue;


    public SaleDataFragment() {}


    @SuppressLint("ValidFragment")
    public SaleDataFragment(AppMfcProtocol appMfcProtocol) {
        this.appMfcProtocol = appMfcProtocol;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sale_data, container, false);
        edtProgrammedValue = view.findViewById(R.id.txtValorProgramado);
        showProgrammedValue();
        edtLicensePlate =view.findViewById(R.id.edtLicensePlate);
        edtIdentificationCard =view.findViewById(R.id.edtIdentificationCard);
        edtNit =view.findViewById(R.id.edtNit);
        edtMileage =view.findViewById(R.id.edtMileage);
        vehicle = new Vehicle();
        client = new Client();

        Button btnEndSale = view.findViewById(R.id.btnEndSale);
        btnEndSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatingInformation();
            }
        });
        return view;
    }


    private void showProgrammedValue(){
        final int FULL=3,MONEY=2,VOLUME=1;
        String valueProgramming;
        if(appMfcProtocol.getProgramming().getPresetKind()==MONEY ){
            valueProgramming = appMfcProtocol.getProgramming().getQuantity() + "";
            edtProgrammedValue.setText(valueProgramming);
        }else if(appMfcProtocol.getProgramming().getPresetKind()==VOLUME){
            valueProgramming = "Galones: " + transformVolume();
            edtProgrammedValue.setText( valueProgramming);
        }else {
            edtProgrammedValue.setText("Full");
        }
    }


    private String transformVolume(){
        int value = (appMfcProtocol.getProgramming().getQuantity()*10)/100;
        return "" + (value/100) + "," + ((value/10)%10) + (value%10);
    }



    private void validatingInformation(){
        vehicle.setLicense_plate(edtLicensePlate.getEditableText().toString());
        vehicle.setKilometres(edtMileage.getEditableText().toString());
        client.setIdentificationCard(edtIdentificationCard.getEditableText().toString());
        client.setNit(edtNit.getEditableText().toString());
        //pedir venta

        if(!vehicle.getLicense_plate().equals("") && !vehicle.getKilometres().equals("")
                && !client.getIdentificationCard().equals("") && !client.getNit().equals("")){
            if(appMfcProtocol.getSale()!=null){
                Sale sale = appMfcProtocol.getSale();
                sale.setClient(client);//revisar comportamiento
                sale.setVehicle(vehicle);
                saleOption.endSale(sale);
            }else {
                Toast.makeText(getContext(),"Espere un momento y presione SIGUIENTE", Toast.LENGTH_SHORT ).show();
            }
        }else{
            Toast.makeText(getContext(), "Ingrese todos los datos por favor", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }


}
