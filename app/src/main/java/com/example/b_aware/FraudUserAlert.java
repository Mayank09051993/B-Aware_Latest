package com.example.b_aware;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

public class FraudUserAlert {

    public static void alertUser(){

        Log.v("FraudUserAlert", "Risky Call");

        createNotification();
        vibrateUser();
    }

    private static void vibrateUser(){
//        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        if (Build.VERSION.SDK_INT >= 26) {
//            vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.EFFECT_HEAVY_CLICK));
//        }
    }

    private static void createNotification() {

//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        Notification notification = new Notification.Builder(this)
//                .setContentTitle("Fraud Call Alert.")
//                .setContentText("Sensitive Information Asked.").setSmallIcon(R.drawable.baware_icon)
//                .setContentIntent(pIntent)
//                .build();
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        notificationManager.notify(0, notification);

    }

}
