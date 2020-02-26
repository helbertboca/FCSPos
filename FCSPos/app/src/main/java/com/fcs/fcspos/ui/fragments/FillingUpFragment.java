package com.fcs.fcspos.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fcs.fcspos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FillingUpFragment extends Fragment {


    public FillingUpFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filling_up, container, false);
        Button btnNewPosition = view.findViewById(R.id.btnNewPosition);
        btnNewPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Solicitando diferente posicion<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        });
        return view;
    }

}
