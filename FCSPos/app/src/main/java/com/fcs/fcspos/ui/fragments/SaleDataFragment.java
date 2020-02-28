package com.fcs.fcspos.ui.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.AppMfcProtocol;
import com.fcs.fcspos.model.Client;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Sale;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.model.Vehicle;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaleDataFragment extends Fragment {


    private Programming programming;
    private SaleOption saleOption;
    private AppMfcProtocol appMfcProtocol;

    public SaleDataFragment() {}

    @SuppressLint("ValidFragment")
    public SaleDataFragment(Programming programming, AppMfcProtocol appMfcProtocol) {
        this.programming = programming;
        this.appMfcProtocol = appMfcProtocol;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_data, container, false);
        final EditText edtLicensePlate = view.findViewById(R.id.edtLicensePlate);
        final EditText edtIdentificationCard = view.findViewById(R.id.edtIdentificationCard);
        final EditText edtNit = view.findViewById(R.id.edtNit);
        final EditText edtMileage = view.findViewById(R.id.edtMileage);
        Button btnEndSale = view.findViewById(R.id.btnEndSale);
        btnEndSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //programming.getVehicle().setLicense_plate(edtLicensePlate.getText().toString());
                //programming.getVehicle().setKilometres(edtMileage.getText().toString());
                //----------------------------------------------------------------------------------
                //puede haber un cambio de posicion que haria que se pierda de momento el vehiculo de la programacion
                //----------------------------------------------------------------------------------
                Vehicle vehicle = new Vehicle();
                vehicle.setLicense_plate(edtLicensePlate.getText().toString());
                vehicle.setKilometres(edtMileage.getText().toString());
                Client client = new Client();
                client.setIdentificationCard(edtIdentificationCard.getText().toString());
                client.setNit(edtNit.getText().toString());
                //pedir venta

                if(appMfcProtocol.getSale()!=null && client.getIdentificationCard()!=null){//revisar que cuando no se llenan los datos de la venta al parecer se bloquea
                    Sale sale = appMfcProtocol.getSale();
                    sale.setClient(client);
                    sale.setVehicle(vehicle);
                    saleOption.endSale(sale);
                }else {
                    Toast.makeText(getContext(),"Venta no reportada, presione SIGUIENTE", Toast.LENGTH_SHORT ).show();
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
