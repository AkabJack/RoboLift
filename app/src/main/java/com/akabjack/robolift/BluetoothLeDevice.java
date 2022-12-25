package com.akabjack.robolift;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;


import java.util.ArrayList;

public class BluetoothLeDevice {
    private BluetoothManager bluetoothManager;
    private BluetoothLeScanner bluetoothScanner;
    private BluetoothAdapter bluetoothAdapter;
    ArrayList<String> DevicesNames = new ArrayList<String>();
    ScanCallback callBack = null;

    public void setCallBack(ScanCallback callBack) {
        this.callBack = callBack;
    }

    public ScanCallback getCallBack() {
        return callBack;
    }

    public BluetoothLeScanner getBlScan() {
        return bluetoothScanner;
    }
    public void setBlScan(){
        bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
    }
    public void setBlScan(BluetoothLeScanner btScan) {
        this.bluetoothScanner = btScan;
    }

    public BluetoothAdapter getBtAdapter() {
        return bluetoothAdapter;
    }
    public void setBtAdapter(){
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            //Toast.makeText(this, "Nu suporta Bluetooth", Toast.LENGTH_SHORT).show();
        }
        else {
            this.bluetoothAdapter = bluetoothManager.getAdapter();
        }

    }
    public void setBtAdapter(BluetoothAdapter btAdapter) {
        this.bluetoothAdapter = btAdapter;
    }

    public ArrayList<String> getDevicesNames() {
        return DevicesNames;
    }
}
