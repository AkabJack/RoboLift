package com.akabjack.robolift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private BluetoothLeScanner blScan;
    private BluetoothAdapter btAdapter;
    ScanCallback callBack;
    ArrayList<ScanResult> rezultate = new ArrayList<>();
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSearch){
            Toast.makeText(this, "Button Search Pressed", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                blScan.startScan(callBack);//TODO add Tread
            }, 100);
            blScan.stopScan((callBack));

        }
        else if(view.getId() == R.id.btnJos){
            Toast.makeText(this, "Button Down Pressed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView afisaj = findViewById(R.id.listBTDev);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnControlJos = findViewById(R.id.btnJos);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(this, "Nu suporta Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);

            final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    final String action = intent.getAction();

                    if (action == null || action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                        //TODO Maybe nullPointerException, try catch
                    final int state = enableBtIntent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    switch (state){
                        case BluetoothAdapter.STATE_OFF:
                            Toast.makeText(context, "BT is off", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Toast.makeText(context, "BT is turning off", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Toast.makeText(context, "BT is on", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Toast.makeText(context, "BT is turning on", Toast.LENGTH_SHORT).show();
                            break;}
                    }
                }
            };

            IntentFilter enableBtIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, enableBtIntentFilter);

            btnSearch.setOnClickListener(this);
            btnControlJos.setOnClickListener(this);
            blScan = btAdapter.getBluetoothLeScanner();
            callBack = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    rezultate.add(result);
                }
            };


            ArrayList<String>conversieRezultate = new ArrayList<String>();
            for (ScanResult rez : rezultate){
                conversieRezultate.add(rez.getDevice().getName()+ " " +  rez.getDevice().getAddress());
            }
            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, conversieRezultate);
            afisaj.setAdapter(listAdapter);

        }


    }

}

