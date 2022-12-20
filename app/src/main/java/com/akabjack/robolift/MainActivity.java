package com.akabjack.robolift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView btListView;
    private static final long SCAN_PERIOD = 1000;//scanam 10 secunde!
    private boolean scanning;
    private Handler handler = new Handler();
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private BluetoothLeScanner btLeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btListView = findViewById(R.id.listBTDev);
        BluetoothManager btManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter btAdapter = btManager.getAdapter();
        BluetoothLeScanner btLeScanner = btAdapter.getBluetoothLeScanner();
        if (btAdapter == null) {
            Toast.makeText(this, "Nu este suportat BT", Toast.LENGTH_SHORT).show();
        }

        //Set<BluetoothDevice> btConectate = btAdapter.getBondedDevices();//TODO plm mai am de lucru
//
        //ArrayList<String> btConectateString = new ArrayList<String>();
        //for (BluetoothDevice device : btConectate) {
        //    btConectateString.add(device.getName() + " " + device.getAddress());//TODO plm mai am de lucru si aci!
        //}
        //
        //ArrayAdapter<String> btListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, btConectateString); //adapter pt afisarea dev BT
        //btListView.setAdapter(btListAdapter);

    }
    private ScanCallback scanLeDevice(){//TODO VERIFICA PERMISIUNILE UTILIZATORULUI
        ScanCallback leScanCallBack;
        if(!scanning){
            //Oprim scanarea
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {scanning = false; btLeScanner.stopScan(leScanCallBack);}
            }, SCAN_PERIOD);
            scanning = true;
            btLeScanner.startScan(leScanCallback);
        }
        else{
            scanning = false;
            btLeScanner.stopScan((leScanCallBack));
        }
        return leScanCallBack;
    }
}

