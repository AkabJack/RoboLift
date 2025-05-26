package com.akabjack.robolift;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
@Deprecated
public class BluetoothConnection {
    private BluetoothManager bluetoothManager;
    private BluetoothLeScanner bluetoothScanner;
    private BluetoothAdapter bluetoothAdapter = null;
    ArrayList<String> DevicesNames = new ArrayList<>();
    ScanCallback callBack;

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
    public void setBtAdapter(android.content.Context context){
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            Toast.makeText(context, "Nu suportă Bluetooth", Toast.LENGTH_SHORT).show();
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

    public void setBluetoothManager(BluetoothManager bluetoothManager) {
        this.bluetoothManager = bluetoothManager;
    }
    public void checkPermissionGranted(Context context) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN}, 2);
                }
            }
        }
    }
}
