package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.OpcionMenu;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuPrincipal extends Fragment {


    private OpcionMenu opcionMenu;
    private final int VENTAS=1;

    public MenuPrincipal() {}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_principal, container, false);
        Button button = view.findViewById(R.id.btnVentas);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opcionMenu.opcionMenuElegida(VENTAS);
            }
        });
        return view;
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        opcionMenu = (OpcionMenu) activity;
    }



}
