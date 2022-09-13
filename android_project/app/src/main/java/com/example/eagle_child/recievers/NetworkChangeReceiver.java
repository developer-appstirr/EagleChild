package com.example.eagle_child.recievers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.eagle_child.helpers.common.NetworkUtils;


public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            if (NetworkUtils.isOnline(context)) {
                //  HomeActivity.networkStatusText(context, true);
                //  ((BaseActivity)context).changeActivity(HomeActivity.class, true);

            } else {

//                Intent intentLogin = new Intent(context, NoNetworkActivity.class);
//                context.startActivity(intentLogin);
//                ((Activity)context).finish();

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}