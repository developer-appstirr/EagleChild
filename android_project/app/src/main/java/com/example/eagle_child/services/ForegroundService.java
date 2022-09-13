package com.example.eagle_child.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.eagle_child.R;

import java.util.Objects;

public class ForegroundService extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Toast.makeText(getApplicationContext(),"Service on start",Toast.LENGTH_SHORT).show();

        return null;
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {


        startInForeground();

        MediaProjection mediaProjection = ((MediaProjectionManager) Objects.requireNonNull(getSystemService(Context.MEDIA_PROJECTION_SERVICE))).
                getMediaProjection(intent.getIntExtra("resultCode",0),intent.getSelector());

    /*    handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                MediaProjection mediaProjection = ((MediaProjectionManager) Objects.requireNonNull(getSystemService(Context.MEDIA_PROJECTION_SERVICE))).
                getMediaProjection(intent.getIntExtra("resultCode",0),intent.getSelector());
            }
        }, 100);
*/        Toast.makeText(getApplicationContext(),"Service on start",Toast.LENGTH_SHORT).show();

     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaProjectionManager projectionManager = (MediaProjectionManager)
                        getSystemService(Context.MEDIA_PROJECTION_SERVICE);


                  MediaProjection  mMediaProjection =
                            projectionManager.getMediaProjection(intent.getIntExtra("resultCode",0), intent.getSelector());
            }
        }, 5000);*/

        return super.onStartCommand(intent, flags, startId);



    }

    private void startInForeground() {

    /*    Intent notificationIntent = new Intent(this, ScreenRecordCustom.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this)
                        .setContentTitle("TEST")
                        .setContentText("HELLO")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentIntent(pendingIntent)
                        .setTicker("TICKER")
                        .build();

        startForeground(100000, notification);
*/

        Intent notificationIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1212, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationManager.EXTRA_NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        builder.setContentTitle("title");
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(100000, notification);
        Log.d("TAG","Start foreground service");




    }


}
