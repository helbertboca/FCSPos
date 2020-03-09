package com.fcs.fcspos.ui.fragments;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fcs.fcspos.R;
import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Receipt;
import com.fcs.fcspos.model.Sale;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.model.Station;

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
    private SaleOption saleOption;
    private Sale sale;
    private Station station;
    private Programming programming;
    private boolean printerFound;
    private ProgressBar pbPrinter;
    private byte numberOfReceipts;



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
                    PrinterWait printerWait = new PrinterWait();
                    printerWait.execute();
                    numberOfReceipts=1;
                }
            }
        });
        btn2Recibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothActivado()){
                    PrinterWait printerWait = new PrinterWait();
                    printerWait.execute();
                    numberOfReceipts=2;
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
                        printerFound = true;
                    }else{
                        impresoraBluetoothDevice = null;
                        printerFound = false;
                    }
                }else{
                    printerFound = false;
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
    }


    //----------------------------------------------------------------------------------------------
    private class PrinterWait extends AsyncTask<String, Void, Boolean> {

        private ProgressBar pb;

        protected void onPreExecute(){
            this.pb = pbPrinter;
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            int count=0;
            bluetoothAdapter.startDiscovery();
            while (count<=7){
                System.out.println("escaneando...");
                SystemClock.sleep(1000);
                if(printerFound){
                    return true;
                }
                count++;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean respuesta){
            pb.setVisibility(View.GONE);
            if(respuesta){
                if(impresoraBluetoothDevice!=null){
                    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    PrimeThread p = new PrimeThread(143, impresoraBluetoothDevice, MY_UUID, bluetoothAdapter);
                    p.start();
                    SystemClock.sleep(2500);
                    if(p.getIsconnect()){
                        byte[] byteArr = new Receipt(station, sale, programming).build(numberOfReceipts).getBytes();
                        p.write(byteArr);
                        //saleOption.receipt((short) 1);
                    }else {
                        Toast.makeText(getContext(), "No se logro conectar a la impresora", Toast.LENGTH_SHORT).show();
                    }
                    p.cancel();
                }
                else {
                    Toast.makeText(getContext(), "No se encontro la impresora, presione nuevamente", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "La impreasora esta fuera del rango de visibilidad", Toast.LENGTH_SHORT).show();
            }
        }

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
                mmSocket.connect();
            } catch (IOException connectException) {
                connectException.printStackTrace();
                connectException.getMessage();
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


        private void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }

        private void write(byte[] bytes) {
            try {
                OutputStream mmOutStream = mmSocket.getOutputStream();
                mmOutStream.write(bytes);

            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);
            }
        }

        private boolean isconnect;

        private void setIsconnect(boolean isconnect){
            this.isconnect = isconnect;
        }

        private boolean getIsconnect(){
            return isconnect;
        }


    }


}
