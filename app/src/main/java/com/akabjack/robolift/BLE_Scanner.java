package com.akabjack.robolift;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

public class BLE_Scanner {
    private MainActivity mainActivity;
    private BluetoothAdapter btAdapter;
    private boolean btScanning = false;
    private Handler btHandler;
    private long scanPeriod;
    private int signalStrenght;
    public BLE_Scanner(MainActivity mainActivity, long scanPeriod, int signalStrenght){
        this.mainActivity = mainActivity;
        btHandler = new Handler();
        this.scanPeriod = scanPeriod;
        this.signalStrenght = signalStrenght;

       final BluetoothManager bluetoothManager = (BluetoothManager) mainActivity.getSystemService(Context.BLUETOOTH_SERVICE);
       btAdapter = bluetoothManager.getAdapter();
    }

    public boolean isScanning(){
        return btScanning;
    }

    public void start(){
         if(Utils.checkBluetooth(btAdapter)){
            Utils.requestUserBluetooth(mainActivity);
            mainActivity.stopScan();
        }
        else {
            scanLeDevice(true);
        }
    }
    public void stop(){
        scanLeDevice(true);
    }

    public void scanLeDevice(final boolean enable){
        if(enable && !btScanning){

            Utils.toast(mainActivity.getApplicationContext(), "Starting BLE scan");
            btHandler.postDelayed(new Runnable() {
                @Override public void run() {
                    Utils.toast(mainActivity.getApplicationContext(), "Stopping BLE scan");
                    btScanning = false;
                    btAdapter.stopLeScan(btScanCallBack);//TODO replace the code with BluetoothLeScanner class
                    mainActivity.stopScan();
                }
            }, scanPeriod);

            btScanning = true;
            btAdapter.startLeScan(btScanCallBack);
        }
    }
    private BluetoothAdapter.LeScanCallback btScanCallBack = new BluetoothAdapter.LeScanCallback() {
        @Override public void onLeScan(final BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {

            final int new_rssi = rssi;
            if (rssi < signalStrenght){
                btHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.addDevice(bluetoothDevice, new_rssi);

                    }
                });
            }
        }
    };
}
