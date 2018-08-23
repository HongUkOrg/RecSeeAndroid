package com.example.user.database.Receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

public class Myreceiver2 extends NotificationListenerService
{


    private MyReceiver myReceiver;
    private String TAG = this.getClass().getSimpleName();






    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "id_product";
            // The user-visible name of the channel.
            CharSequence name = "Product";
            // The user-visible description of the channel.
            String description = "Notifications regarding our products";
            int importance = NotificationManager.IMPORTANCE_MAX;
            @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(mChannel);
        }
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn)
    {
//        IntentFilter filter = new IntentFilter();

//        Toast.makeText(this,sbn.getPackageName()+" "+sbn.getNotification().tickerText+" plus "+sbn.getNotification().extras.getString(Notification.EXTRA_TEXT
//        ),Toast.LENGTH_SHORT).show();
        
        //Log.i(TAG, "Notification Posted");
//        Log.i(TAG, sbn.getPackageName() +
//                "\t" + sbn.getNotification().tickerText +
//                "\t" + sbn.getNotification().extras.getString(Notification.EXTRA_TEXT));


        Bundle extras = sbn.getNotification().extras;

        if ("Ongoing call".equals(extras.getString(Notification.EXTRA_TEXT))||"현재 통화".equals(extras.getString(Notification.EXTRA_TEXT)))
        {
//            Log.i(TAG, "Notification Log : Ongoing Call ");
            //Toast.makeText(this,"Call Connected",Toast.LENGTH_SHORT).show();
            myReceiver.outgoing_missed=false;


        }
        else if ("Dialing".equals(extras.getString(Notification.EXTRA_TEXT))||"전화 연결 중".equals(extras.getString(Notification.EXTRA_TEXT)))
        {
//            Log.i(TAG, "Notification Log : Dialing");
            //Toast.makeText(this,"Dialing deteced",Toast.LENGTH_SHORT).show();
            myReceiver.outgoing_missed=true;


        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {


//        Toast.makeText(this,"noti quit",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
}
