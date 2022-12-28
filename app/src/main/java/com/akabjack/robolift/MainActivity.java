package com.akabjack.robolift;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ListView afisaj;
    ArrayAdapter<String> listAdapter;
    BluetoothLeDevice btConnection;
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSearch){//TODO IF PRESSED THE APP REMAINS FROZEN
            Toast.makeText(this, "Button Search Pressed", Toast.LENGTH_SHORT).show();
            long endTime = System.currentTimeMillis() + 5000;
            boolean isScanning = false;
            while(System.currentTimeMillis() < endTime){
                if(!isScanning){
                    btConnection.getBlScan().startScan(btConnection.getCallBack());//TODO Beautify
                    isScanning = true;
                }
            }
            btConnection.getBlScan().stopScan((btConnection.getCallBack()));
            listAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Finished searching", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.btnJos){
            Toast.makeText(this, "Button Down Pressed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnControlJos = findViewById(R.id.btnJos);
        afisaj = findViewById(R.id.listBTDev);

        Toast.makeText(MainActivity.this, "App Init", Toast.LENGTH_SHORT).show();
        btConnection = new BluetoothLeDevice();
        btConnection.setBluetoothManager((BluetoothManager) getSystemService(BLUETOOTH_SERVICE)); //TODO Study this
        btConnection.setBtAdapter(this);//nullPointter
        btConnection.setCallBack(new ScanCallback() {
            @Override public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice device = result.getDevice();
                String deviceName = device.getName();
                btConnection.DevicesNames.add(deviceName);
            }
        });
        BluetoothAdapter btAdapter = btConnection.getBtAdapter();//TODO repair this

        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                @Override public void onReceive(Context context, Intent intent) {
                    final String action = intent.getAction();
                    if (action == null || action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                        //TODO Maybe nullPointerException, try catch
                        final int state = enableBtIntent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                        switch (state) {
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
                                break;
                        }
                    }
                }
            };
            IntentFilter enableBtIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, enableBtIntentFilter);
        }
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

            btConnection.setBlScan();

            btnSearch.setOnClickListener(this);
            btnControlJos.setOnClickListener(this);
            listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, btConnection.getDevicesNames());
            afisaj.setAdapter(listAdapter);

        }
    }
