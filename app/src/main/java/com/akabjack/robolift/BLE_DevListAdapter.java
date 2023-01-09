package com.akabjack.robolift;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BLE_DevListAdapter extends ArrayAdapter<BLE_Device> {
    Activity activity;
    int layoutResId;
    ArrayList<BLE_Device> devices;

    public BLE_DevListAdapter(Activity activity, int resource, ArrayList<BLE_Device> objects){
        super(activity.getApplicationContext(),resource,objects);
        this.activity = activity;
        this.layoutResId = resource;
        this.devices = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){//TODO Study
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResId, parent , false);
        }
        BLE_Device device = devices.get(position);
        String name = device.getName();
        String address = device.getAddress();
        int rssi = device.getRssi();

        TextView txtName = (TextView) convertView.findViewById(R.id.tv_name);//TODO Look at this;
        if (name != null && name.length() >0){
            txtName.setText(device.getName());
        }
        else {
            txtName.setText("No name");
        }

        TextView txtRssi = (TextView) convertView.findViewById(R.id.tv_rssi);
        txtRssi.setText(String.format("RSSI:%s", Integer.toString(rssi)));

        TextView txtMacAddress = (TextView) convertView.findViewById(R.id.tv_macaddr);
        if (address != null && address.length() > 0){
            txtMacAddress.setText((device.getAddress()));
        }
        else{
            txtMacAddress.setText("No Address");
        }
        return convertView;
    }
}
