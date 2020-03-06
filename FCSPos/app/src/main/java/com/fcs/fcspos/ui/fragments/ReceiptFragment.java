package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.Sale;
import com.fcs.fcspos.model.SaleOption;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptFragment extends Fragment {


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice impresoraBluetoothDevice;
    private PrimeThread p;
    private SaleOption saleOption;
    private Sale sale;


    public ReceiptFragment(Sale sale) {
        this.sale = sale;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
        Button btn1Receipt = view.findViewById(R.id.btn1Recibo);
        Button btn2Receipts = view.findViewById(R.id.btn2Recibo);
        Button btn3Recibo = view.findViewById(R.id.btn3Recibo);
        btn1Receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothActivado()){
                    if(bluetoothAdapter.startDiscovery()){
                        System.out.println("escaneo");
                    }
                }
                //saleOption.receipt((short) 1);
            }
        });
        btn2Receipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(impresoraBluetoothDevice!=null){
                    System.out.println("Se iniciara conexion: " + impresoraBluetoothDevice.getName() + impresoraBluetoothDevice.getAddress());
                    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    p = new PrimeThread(143, impresoraBluetoothDevice, MY_UUID, bluetoothAdapter);
                    p.start();
                    System.out.println("INICIO HILO UP");
                }
                //saleOption.receipt((short) 2);
            }
        });


        btn3Recibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p.getIsconnect()){
                    String str = sale.toString();
                    byte[] byteArr = str.getBytes();
                    p.write(byteArr);
                    p.cancel();
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
                if(device.getName().equals("SUNPHOR")){
                    System.out.println("Se encontro la impresora; " + device.getName());
                    impresoraBluetoothDevice = device;
                    bluetoothAdapter.cancelDiscovery();
                }else{
                    impresoraBluetoothDevice = null;
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
            System.out.println("dispositivo Compatible con bluetooth");        //se envia automaticamente un mensaje pidiendo habilitar el bluetooth al usuario
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
        //unregisterReceiver(receiver);
    }

    //----------------------------------------------------------------------------------------------
    class PrimeThread extends Thread {

        long minPrime;
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private BluetoothAdapter bluetoothAdapter;

        PrimeThread(long minPrime, BluetoothDevice device, UUID MY_UUID, BluetoothAdapter bluetoothAdapter) {
            this.minPrime = minPrime;
            this.bluetoothAdapter =bluetoothAdapter;
            BluetoothSocket tmp = null;
            this.mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();
            try {
                System.out.println("1");
                mmSocket.connect();
            } catch (IOException connectException) {
                connectException.printStackTrace();
                connectException.getMessage();
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                setIsconnect(false);
                return;
            }

            System.out.println("LA CONEXION FUE EXITOSA CON LA IMPRESORA");
            setIsconnect(true);
        }


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }

        public void write(byte[] bytes) {
            try {
                OutputStream mmOutStream = mmSocket.getOutputStream();
                mmOutStream.write(bytes);

            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);
            }
        }

        private boolean isconnect;

        public void setIsconnect(boolean isconnect){
            this.isconnect = isconnect;
        }

        public boolean getIsconnect(){
            return isconnect;
        }


    }
    //----------------------------------------------------------------------------------------------




}
