package com.akabjack.robolift;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.SystemClock;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BLE_Connection implements Parcelable {
    private BluetoothDevice btDevice;
    private boolean isConnected = false;
    private BluetoothGatt btDevGatt;
    private List<BluetoothGattService> services;
    private BluetoothGattService selectedServ;
    private BluetoothGattCharacteristic selectedChar;
    private ParcelUuid selectedServiceUUID = null;
    private ParcelUuid selectedCharacUUID = null;
    public BLE_Connection(BluetoothDevice btDevice) {
        this.btDevice = btDevice;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(btDevice, flags);
        dest.writeParcelable(selectedServiceUUID,flags);
        dest.writeParcelable(selectedCharacUUID,flags);

    }
    protected BLE_Connection(Parcel in){
        this.btDevice = in.readParcelable(BLE_Connection.class.getClassLoader());
        this.selectedServiceUUID = in.readParcelable(BLE_Connection.class.getClassLoader());
        this.selectedCharacUUID = in.readParcelable(BLE_Connection.class.getClassLoader());
    }

    public static final Parcelable.Creator<BLE_Connection> CREATOR = new Parcelable.Creator<BLE_Connection>() {
        @Override
        public BLE_Connection createFromParcel(Parcel parcel) {
          return new BLE_Connection(parcel);
        }

        @Override
        public BLE_Connection[] newArray(int i) {
            return new BLE_Connection[i];
        }
    };

    private BluetoothGattCallback gattInitialCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState ==  BluetoothProfile.STATE_CONNECTED){
                gatt.discoverServices();
                isConnected = true;
            }
            else {isConnected = false;}
        }
        @Override public void onServicesDiscovered (BluetoothGatt gatt, int status){
            if(status == BluetoothGatt.GATT_SUCCESS){
                services = btDevGatt.getServices();
            }
        }
    };

    public boolean isConnected(){
        return isConnected;
    }

    public void connectGatt(Context context, boolean autoConnect){
        btDevGatt = btDevice.connectGatt(context, autoConnect, gattInitialCallback);
        if(services == null) {btDevGatt.discoverServices();}
        isConnected = true;
    }
    public void disconnectGatt(){
        if (btDevGatt != null){
            btDevGatt.disconnect();
        }
    }

    public BluetoothGattService getService(UUID uuid) {
        for (BluetoothGattService serv : services) {
            if (serv.getUuid().toString().equals(uuid.toString())) {
                setSelectedServ(serv);
                return serv;
            }
        }
        return null;
    }

    public BluetoothGattService getSelectedServ() {
        return selectedServ;
    }

    public void setSelectedServ(BluetoothGattService selectedServ) {
        this.selectedServ = selectedServ;
    }

    public BluetoothGattCharacteristic getCharact(UUID uuid){
        for(BluetoothGattCharacteristic characteristic : selectedServ.getCharacteristics()){
            if(characteristic.getUuid().toString().equals(uuid.toString())){
                setSelectedChar(characteristic);
                setSelectedCharacUUID(new ParcelUuid(uuid));
                return characteristic;
            }
        }
        return null;
    }

    public BluetoothGattCharacteristic getCharact(@NonNull BluetoothGattService service, UUID uuid){
        for(BluetoothGattCharacteristic characteristic : service.getCharacteristics()){
            if(characteristic.getUuid() == uuid){
                setSelectedChar(characteristic);
                return characteristic;
            }
        }
        return null;
    }

    public BluetoothGattCharacteristic getSelectedChar() {
        return selectedChar;
    }

    public void setSelectedChar(BluetoothGattCharacteristic selectedChar) {
        this.selectedChar = selectedChar;
    }

    public boolean sendCommand(byte[] data){
        if(selectedChar == null){
            getService(getSelectedServiceUUID().getUuid());
            getCharact(getSelectedCharacUUID().getUuid());
            selectedChar.setValue(data);
            return btDevGatt.writeCharacteristic(selectedChar);
        }else{
            selectedChar.setValue(data);
            return btDevGatt.writeCharacteristic(selectedChar);
        }
    }

    public boolean sendCommand(@NonNull BluetoothGattCharacteristic characteristic, byte[] data){
        characteristic.setValue(data);
        return btDevGatt.writeCharacteristic(characteristic);
    }

    public List<BluetoothGattService> getServices() {
        return services;
    }

    public ArrayList<String> getServicesAsString(){
        ArrayList<String> list = new ArrayList<>();
        for (BluetoothGattService serv : services){
            String value = new ParcelUuid(serv.getUuid()).toString();
            list.add(value);
        }
        return list;
    }
    public ArrayList<String> getCharateristicsAsString(){
        ArrayList<String> list = new ArrayList<>();
        for (BluetoothGattCharacteristic characteristic : selectedServ.getCharacteristics()){
            list.add(new ParcelUuid(characteristic.getUuid()).toString());
        }
        return list;
    }
    public ParcelUuid getSelectedCharacUUID() {
        return selectedCharacUUID;
    }

    public void setSelectedCharacUUID(ParcelUuid selectedCharacUUID) {
        this.selectedCharacUUID = selectedCharacUUID;
    }

    public ParcelUuid getSelectedServiceUUID() {
        return selectedServiceUUID;
    }

    public void setSelectedServiceUUID(ParcelUuid selectedServiceUUID) {
        this.selectedServiceUUID = selectedServiceUUID;
    }
    public String isConectedCast(){
        if(isConnected){
            return new String("Este conectat");
        }
        else return new String("Nu este conectat");
    }
}
