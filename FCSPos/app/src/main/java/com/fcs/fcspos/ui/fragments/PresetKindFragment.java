package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.SaleOption;

/**
 * A simple {@link Fragment} subclass.
 */
public class PresetKindFragment extends Fragment {


    private SaleOption saleOption;
    private final int FULL=3,MONEY=2,VOLUME=1;


    public PresetKindFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_preset_kind, container, false);
        Button btnMoneyPreset =view.findViewById(R.id.btnMoneyPreset);
        Button btnVolumePreset =view.findViewById(R.id.btnVolumePreset);
        Button btnFullPreset =view.findViewById(R.id.btnFullPreset);
        btnMoneyPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionPresetKind(MONEY);
            }
        });
        btnVolumePreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionPresetKind(VOLUME);
            }
        });
        btnFullPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionPresetKind(FULL);
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
