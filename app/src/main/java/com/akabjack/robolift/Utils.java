package com.akabjack.robolift;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.Gravity;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class Utils {

    public static boolean checkBluetooth(BluetoothAdapter btAdapter) {
        if(btAdapter == null || btAdapter.isEnabled()){
            return false;
        }
        else return true;
    }

    public static void requestUserBluetooth(Activity activity) {//TODO Be carefull
        Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions((Activity) activity, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN},
                        2);
            }
            else{
                ActivityCompat.requestPermissions((Activity) activity, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,},
                        2);
            }
        }
        activity.startActivityForResult(enableBTIntent, MainActivity.REQUEST_ENABLE_BT);
    }

    public static void toast(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0,0);
        toast.show();
    }
}
