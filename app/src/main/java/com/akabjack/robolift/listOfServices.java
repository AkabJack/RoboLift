package com.akabjack.robolift;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class listOfServices extends AppCompatActivity implements View.OnClickListener {
    private ListView serviceScrollView;
    private ListView characteristicScrollView;
    private BLE_Connection robotConnection;
    private ParcelUuid selectedServiceUUID;
    private ParcelUuid selectedCharacUUID;
    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_services);

        if (getIntent().getExtras() != null){
            robotConnection = getIntent().getParcelableExtra("BLEConnection");
        }

        robotConnection.connectGatt(this, false);

        final long time = System.currentTimeMillis();
        while(System.currentTimeMillis() <= time + 2000){
            //Stupid fix lmao
            //gatt.discoverServices(); este o metoda asincrona, asa ca am introdus un delay de 2 sec ca sa aiba timp sa caute
        }

        serviceScrollView = findViewById(R.id.serviceListView);
        characteristicScrollView = findViewById(R.id.characteristicListView);

        ArrayAdapter<String> serviceAdapt = new ArrayAdapter<>(listOfServices.this, android.R.layout.simple_list_item_1, robotConnection.getServicesAsString());
        ArrayList<String> characteristics = new ArrayList<>();
        ArrayAdapter<String> chararcAdapter = new ArrayAdapter<>(listOfServices.this, android.R.layout.simple_list_item_1, characteristics);

        serviceScrollView.setAdapter(serviceAdapt);
        characteristicScrollView.setAdapter(chararcAdapter);

        serviceScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedServiceUUID = new ParcelUuid(robotConnection.getServices().get(position).getUuid());
                robotConnection.getService(robotConnection.getServices().get(position).getUuid());
                robotConnection.setSelectedServiceUUID(selectedServiceUUID);
                characteristics.clear();
                characteristics.addAll(robotConnection.getCharateristicsAsString());
                chararcAdapter.notifyDataSetChanged();
            }
        });

        characteristicScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCharacUUID = new ParcelUuid(robotConnection.getSelectedServ().getCharacteristics().get(position).getUuid());
                robotConnection.getCharact(selectedCharacUUID.getUuid());
            }
        });

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent dataTran = new Intent();
                dataTran.putExtra("selectedServiceUUID", selectedServiceUUID.toString());
                dataTran.putExtra("selectedCharacUUID", selectedCharacUUID.toString());
                setResult(RESULT_OK, dataTran);
                finish();
            }
        });
    }
}