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
public class PresetKindFragment extends Fragment {


    private SaleOption saleOption;
    private final int FULL=3,MONEY=2,VOLUME=1;


    public PresetKindFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_preset_kind, container, false);

        LinearLayout llMoneyPreset = view.findViewById(R.id.llMoneyPreset);
        LinearLayout llVolumePreset = view.findViewById(R.id.llVolumePreset);
        LinearLayout llFullPreset = view.findViewById(R.id.llFullPreset);
        llMoneyPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionPresetKind(MONEY);
            }
        });
        llVolumePreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.optionPresetKind(VOLUME);
            }
        });
        llFullPreset.setOnClickListener(new View.OnClickListener() {
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
