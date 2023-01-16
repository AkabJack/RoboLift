package com.akabjack.robolift;
//TODO buton de stop commanda si implement celorlalte butonae
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_ENABLE_BT = 1;
    public static final String TAG = MainActivity.class.getSimpleName();
    private HashMap<String, BLE_Device> btDevHashMap;
    private ArrayList<BLE_Device> btDevArrayList;
    private BLE_DevListAdapter adapter;
    private BtBroadcastReceiver btStateUpdateReceiver;
    private BLE_Scanner ble_scanner;
    private ScrollView afisaj;
    TextView txtServ;
    TextView txtChar;
    TextView txtStatusCon;
    private BLE_Connection robotConnection;
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        //https://developer.android.com/training/basics/intents/result#java
        @Override public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                Intent intent = result.getData();
                txtServ.setText((String) intent.getStringExtra("selectedServiceUUID"));
                txtChar.setText((String) intent.getStringExtra("selectedCharacUUID"));
                getRobotConnection().setSelectedServiceUUID(ParcelUuid.fromString((String) intent.getStringExtra("selectedServiceUUID")));
                getRobotConnection().setSelectedCharacUUID(ParcelUuid.fromString((String) intent.getStringExtra("selectedCharacUUID")));
            }
        }
    });

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSearch) {
            if(!ble_scanner.isScanning()){startScan();}
            else{stopScan();}
        }
        else if(view.getId() == R.id.btnServiceSelect){
            Intent intent = new Intent(MainActivity.this, listOfServices.class);
            intent.putExtra("BLEConnection", robotConnection);
            launcher.launch(intent);
        }
        else if(view.getId() == R.id.btnConnRetrieve){
            robotConnection.connectGatt(MainActivity.this, true);
            txtStatusCon.setText(robotConnection.isConectedCast());
        }
        else if (view.getId() == R.id.btnJos) {
            //Toast.makeText(this, "Button Down Pressed", Toast.LENGTH_SHORT).show();
            if(robotConnection != null){
                String command = new String("1");
                byte[] bit = command.getBytes();
                System.out.println(robotConnection.sendCommand(bit));
            }
        }
        else if(view.getId() == R.id.btnSus){
            if(robotConnection != null){
                String command = new String("2");
                byte[] bit = command.getBytes();
                System.out.println(robotConnection.sendCommand(bit));
            }
        }
        else if(view.getId() == R.id.btnDreapta){
            if(robotConnection != null){
                String command = new String("3");
                byte[] bit = command.getBytes();
                System.out.println(robotConnection.sendCommand(bit));
            }
        }
        else if(view.getId() == R.id.btnStanga){
            if(robotConnection != null){
                String command = new String("4");
                byte[] bit = command.getBytes();
                System.out.println(robotConnection.sendCommand(bit));
            }
        }
        else if(view.getId() == R.id.btnDispenseCandy) {
            if (robotConnection != null) {
                String command = new String("5");
                byte[] bit = command.getBytes();
                System.out.println(robotConnection.sendCommand(bit));
            }
        }
        else if(view.getId() == R.id.btnStopMovement){
            if(robotConnection != null) {
                String command = new String("7");
                byte[] bit = command.getBytes();
                System.out.println(robotConnection.sendCommand(bit));
            }
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
        ble_scanner = new BLE_Scanner(this, 10000, -50);

        btDevHashMap = new HashMap<>();
        btDevArrayList = new ArrayList<>();

        adapter = new BLE_DevListAdapter(this, R.layout.btle_device_list_item, btDevArrayList);

        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        ((ScrollView) findViewById(R.id.listBTDev)).addView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                robotConnection = new BLE_Connection(btDevArrayList.get(position).getBtDevice());
            }
        });
        txtServ = (TextView) findViewById(R.id.txtServ);
        txtChar = (TextView) findViewById(R.id.txtChar);
        txtStatusCon = (TextView) findViewById(R.id.txtStatusCon);
        txtStatusCon.setText("Nu este conectat");

        Button btnControlJos = findViewById(R.id.btnJos);
        Button btnControlSus = findViewById(R.id.btnSus);
        Button btnControlDreapta = findViewById(R.id.btnDreapta);
        Button btnControlStanga = findViewById(R.id.btnStanga);
        Button btnCandy = findViewById(R.id.btnDispenseCandy);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnServiceSelect = findViewById(R.id.btnServiceSelect);
        Button btnConnRetrieve = findViewById(R.id.btnConnRetrieve);
        Button btnStopMovement = findViewById(R.id.btnStopMovement);
        findViewById(R.id.btnJos).setOnClickListener(this);
        findViewById(R.id.btnSus).setOnClickListener(this);
        findViewById(R.id.btnDreapta).setOnClickListener(this);
        findViewById(R.id.btnStanga).setOnClickListener(this);
        findViewById(R.id.btnDispenseCandy).setOnClickListener(this);
        findViewById(R.id.btnStopMovement).setOnClickListener(this);
        findViewById(R.id.btnSearch).setOnClickListener(this);
        findViewById(R.id.btnServiceSelect).setOnClickListener(this);
        findViewById(R.id.btnConnRetrieve).setOnClickListener(this);

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
                    if (getIntent().getExtras() != null){
                        BLE_Connection dummy =  (BLE_Connection) getIntent().getSerializableExtra("BLEConnection");
                        robotConnection.setSelectedCharacUUID(dummy.getSelectedCharacUUID());
                        robotConnection.setSelectedServiceUUID(dummy.getSelectedServiceUUID());
                    }
                }
                else if (resultCode == RESULT_CANCELED){
                    Utils.toast(getApplicationContext(), "Va rugam porniti Bluetooth");
                }
            }
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
            btDevHashMap.get(macAddress).setRssi(rssi);
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

    public BLE_Connection getRobotConnection() {
        return robotConnection;
    }
}
