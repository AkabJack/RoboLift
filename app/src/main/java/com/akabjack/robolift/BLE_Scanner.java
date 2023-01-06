package com.akabjack.robolift;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.os.ParcelUuid;

import java.util.List;

public class BLE_Scanner {
    private MainActivity mainActivity;
    private BluetoothAdapter btAdapter;
    private boolean btScanning = false;
    private Handler btHandler;
    private long scanPeriod;
    private int signalStrenght;
    private BluetoothLeScanner bleScanner;
    private List<ParcelUuid> uuids;
    public BLE_Scanner(MainActivity mainActivity, long scanPeriod, int signalStrenght){
        this.mainActivity = mainActivity;
        btHandler = new Handler();
        this.scanPeriod = scanPeriod;
        this.signalStrenght = signalStrenght;

       final BluetoothManager bluetoothManager = (BluetoothManager) mainActivity.getSystemService(Context.BLUETOOTH_SERVICE);
       btAdapter = bluetoothManager.getAdapter();
       this.bleScanner = btAdapter.getBluetoothLeScanner();
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
        scanLeDevice(false);
    }

    public void scanLeDevice(final boolean enable){
        if(enable && !btScanning){

            Utils.toast(mainActivity.getApplicationContext(), "Starting BLE scan");
            btHandler.postDelayed(new Runnable() {
                @Override public void run() {
                    Utils.toast(mainActivity.getApplicationContext(), "Stopping BLE scan");
                    btScanning = false;
                    bleScanner.stopScan((ScanCallback) btScanCallBack);
                    mainActivity.stopScan();
                }
            }, scanPeriod);

            btScanning = true;
            bleScanner.startScan(btScanCallBack);
        }
        else{
            btScanning = false;
            bleScanner.startScan(btScanCallBack);
        }
    }
    private ScanCallback btScanCallBack = new ScanCallback() {

        public void onScanResult (int callbackType, ScanResult result){
            final BluetoothDevice bluetoothDevice = result.getDevice();
            final int new_rssi = result.getRssi();
            if (new_rssi < signalStrenght){
                btHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.addDevice(bluetoothDevice, new_rssi);
                    }
                });
            }
            uuids = result.getScanRecord().getServiceUuids(); //
        }
    };
}
