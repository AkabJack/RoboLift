package com.akabjack.robolift;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

public class BLE_Device {
    private BluetoothDevice btDevice;
    private int rssi;

    public BLE_Device(BluetoothDevice btDevice) {this.btDevice = btDevice;}

    public int getRssi() {return rssi;}
    public void setRssi(int rssi) {this.rssi = rssi;}
    public String getName(){return btDevice.getName();} //TODO Ask Permission
    public String getAddress(){return btDevice.getAddress();}

    public BluetoothDevice getBtDevice() {
        return btDevice;
    }
}
