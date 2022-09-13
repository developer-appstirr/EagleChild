package com.example.eagle_child.Utils;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceAdminDemo extends DeviceAdminReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("receiver","registered");
        super.onReceive(context, intent);
    }

    public void onEnabled(Context context, Intent intent) {

        Log.i("receiver","enabled");
    }

    public void onDisabled(Context context, Intent intent) {
        Log.i("receiver","disabled");

    }

}