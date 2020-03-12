package com.fcs.fcspos.io;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fcs.fcspos.model.Programming;
import com.fcs.fcspos.model.Receipt;
import com.fcs.fcspos.model.Sale;
import com.fcs.fcspos.model.SaleOption;
import com.fcs.fcspos.model.Station;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class MfcBlueCom {


    private ProgressBar pbPrinter;
    private BluetoothAdapter bluetoothAdapter;
    private boolean printerFound;
    private BluetoothDevice impresoraBluetoothDevice;
    private byte numberOfReceipts;
    private Sale sale;
    private Station station;
    private Programming programming;
    private Context context;
    private SaleOption saleOption;


    public MfcBlueCom(ProgressBar pbPrinter, BluetoothAdapter bluetoothAdapter, BluetoothDevice impresoraBluetoothDevice, byte numberOfReceipts,
                      Sale sale, Station station, Programming programming, Context context, SaleOption saleOption){
        this.pbPrinter = pbPrinter;
        this.bluetoothAdapter = bluetoothAdapter;
        this.impresoraBluetoothDevice = impresoraBluetoothDevice;
        this.numberOfReceipts = numberOfReceipts;
        this.sale = sale;
        this.station = station;
        this.programming = programming;
        this.context = context;
        this.saleOption = saleOption;
    }


    public void setPrinterFound(boolean printerFound) {
        this.printerFound = printerFound;
    }

    public void setImpresoraBluetoothDevice(BluetoothDevice impresoraBluetoothDevice) {
        this.impresoraBluetoothDevice = impresoraBluetoothDevice;
    }

    //----------------------------------------------------------------------------------------------
    public class PrinterWait extends AsyncTask<String, Void, Boolean> {

        private ProgressBar pb;

        protected void onPreExecute(){
            this.pb = pbPrinter;
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            int count=0;
            bluetoothAdapter.startDiscovery();
            while (count<=12){
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
                        for(byte i=0;i<numberOfReceipts;i++){
                            byte[] byteArr = new Receipt(station, sale, programming).build(i).getBytes();
                            p.write(byteArr);
                            SystemClock.sleep(500);
                        }

                        saleOption.receipt();
                    }else {
                        Toast.makeText(context, "No se logro conectar a la impresora", Toast.LENGTH_SHORT).show();
                    }
                    p.cancel();
                }
                else {
                    Toast.makeText(context, "No se encontro la impresora, presione nuevamente", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context, "La impreasora esta fuera del rango de visibilidad", Toast.LENGTH_SHORT).show();
            }
        }

    }


    //----------------------------------------------------------------------------------------------
    private class PrimeThread extends Thread {

        long minPrime;
        private final BluetoothSocket mmSocket;
        private BluetoothAdapter bluetoothAdapter;

        PrimeThread(long minPrime, BluetoothDevice device, UUID MY_UUID, BluetoothAdapter bluetoothAdapter) {
            this.minPrime = minPrime;
            this.bluetoothAdapter =bluetoothAdapter;
            BluetoothSocket tmp = null;
            //this.mmDevice = device;
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
