package com.akabjack.robolift;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int REQUEST_ENABLE_BT = 1;
    public static final String TAG = MainActivity.class.getSimpleName();
    private HashMap<String, BLE_Device> btDevHashMap;
    private ArrayList<BLE_Device> btDevArrayList;
    private BLE_DevListAdapter adapter;
    private BtBroadcastReceiver btStateUpdateReceiver;
    private BLE_Scanner ble_scanner;
    ScrollView afisaj;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSearch) {//TODO IF PRESSED THE APP REMAINS FROZEN
            if(!ble_scanner.isScanning()){startScan();}
            else{stopScan();}
        }
        else if (view.getId() == R.id.btnJos) {
            Toast.makeText(this, "Button Down Pressed", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){//Verificam daca dispozitivul are capabilitati ble
            Utils.toast(getApplicationContext(), "Your device doesn't support BLE");
            finish();
        }
        Utils.toast(getApplicationContext(), "App Init");

        afisaj = findViewById(R.id.listBTDev);
        btStateUpdateReceiver = new BtBroadcastReceiver(getApplicationContext());
        ble_scanner = new BLE_Scanner(this, 7500, -75);

        btDevHashMap = new HashMap<>();
        btDevArrayList = new ArrayList<>();

        adapter = new BLE_DevListAdapter(this, R.layout.btle_device_list_item, btDevArrayList);

        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        ((ScrollView) findViewById(R.id.listBTDev)).addView(listView);

        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnControlJos = findViewById(R.id.btnJos);
        findViewById(R.id.btnJos).setOnClickListener(this);
        findViewById(R.id.btnSearch).setOnClickListener(this);
        }

        @Override protected void onStart(){
            super.onStart();
            registerReceiver(btStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }
        @Override protected void onResume(){
            super.onResume();
        }
        @Override protected void onPause(){
            super.onPause();
            stopScan();
        }
        @Override protected void onStop(){
            super.onStop();
            stopScan();
            unregisterReceiver(btStateUpdateReceiver);
        }
        @Override protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == REQUEST_ENABLE_BT){
                //MAKE SURE THE REQUEST WAS SUCCESSFUL
                if(resultCode == RESULT_OK){
                    //sup!
                }
                else if (resultCode == RESULT_CANCELED){
                    Utils.toast(getApplicationContext(), "Va rugam porniti Bluetooth");
                }
            }
        }
        @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO Implement this
    }

    public void addDevice(BluetoothDevice deviceAdded, int rssi){
        String macAddress = deviceAdded.getAddress();

        if(!btDevHashMap.containsKey(macAddress)){
            BLE_Device ble_device = new BLE_Device(deviceAdded);
            ble_device.setRssi(rssi);

            btDevHashMap.put(macAddress, ble_device);
            btDevArrayList.add(ble_device);
        }
        else {
            btDevHashMap.get(macAddress).setRssi(rssi);//TODO May produce NullPointer
        }
        adapter.notifyDataSetChanged();
    }
    public void startScan(){
        Utils.toast(this,"Scanning");

        btDevHashMap.clear();
        btDevArrayList.clear();
        adapter.notifyDataSetChanged();

        ble_scanner.start();
    }
    public void stopScan(){
        Utils.toast(this,"Scanning stopped");

        ble_scanner.stop();
    }
}
