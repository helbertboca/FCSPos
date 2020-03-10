package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.fcs.fcspos.R;
import com.fcs.fcspos.io.MfcBlueCom;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Sale;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.model.Station;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptFragment extends Fragment {


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice impresoraBluetoothDevice;
    private SaleOption saleOption;
    private Sale sale;
    private Station station;
    private Programming programming;
    private ProgressBar pbPrinter;
    private MfcBlueCom mfcBlueCom;



    public ReceiptFragment(Sale sale, Station station, Programming programming) {
        this.sale = sale;
        this.station = station;
        this.programming = programming;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
        pbPrinter = view.findViewById(R.id.pbPrinter);
        Button btnNingunRecibo = view.findViewById(R.id.btnNingunRecibo);
        Button btn1Recibo = view.findViewById(R.id.btn1Recibo);
        Button btn2Recibo = view.findViewById(R.id.btn2Recibo);
        btnNingunRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleOption.receipt((short) 0);
            }
        });
        btn1Recibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothActivado()){
                    mfcBlueCom = new MfcBlueCom(pbPrinter ,bluetoothAdapter,
                            impresoraBluetoothDevice, (byte)1, sale, station, programming,
                            getContext(), saleOption );
                    MfcBlueCom.PrinterWait printerWait = mfcBlueCom.new PrinterWait();
                    printerWait.execute();
                }
            }
        });
        btn2Recibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothActivado()) {
                    mfcBlueCom = new MfcBlueCom(pbPrinter ,bluetoothAdapter,
                            impresoraBluetoothDevice, (byte)2, sale, station, programming,
                            getContext(), saleOption );
                    MfcBlueCom.PrinterWait printerWait = mfcBlueCom.new PrinterWait();
                    printerWait.execute();
                }
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
        return view;
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        saleOption = (SaleOption) activity;
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                System.out.println("Dispositivo: " + deviceName + deviceHardwareAddress );
                if(device.getName()!=null){
                    if(device.getName().equals("SUNPHOR")){
                        System.out.println("Se encontro la impresora; " + device.getName());
                        impresoraBluetoothDevice = device;
                        bluetoothAdapter.cancelDiscovery();
                        mfcBlueCom.setPrinterFound(true);
                        mfcBlueCom.setImpresoraBluetoothDevice(impresoraBluetoothDevice);
                    }else{
                        impresoraBluetoothDevice = null;
                        mfcBlueCom.setPrinterFound(false);
                        mfcBlueCom.setImpresoraBluetoothDevice(null);
                    }
                }else{
                    mfcBlueCom.setPrinterFound(false);
                    mfcBlueCom.setImpresoraBluetoothDevice(null);
                }
            }
        }
    };


    private boolean bluetoothActivado(){
        final int REQUEST_ENABLE_BT=1;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            System.out.println("dispositivo No compatible con bluetooth");
        }else{
            System.out.println("dispositivo Compatible con bluetooth");
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else{
                System.out.println("dispositivo bluetooth ya estaba encendido");
                return true;
            }
        }
        return false;
    }


    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode == getActivity().RESULT_OK){
                System.out.println("bluetooth habilitado");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
    }


}
