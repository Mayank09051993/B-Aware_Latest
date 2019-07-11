package com.example.b_aware;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

public class FraudUserAlert {

    public static void alertUser(Context context){

        Log.v("FraudUserAlert", "Risky Call");

        createNotification(context);
        vibrateUser(context);
    }

    private static void vibrateUser(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.EFFECT_HEAVY_CLICK));
        }
    }

    private static void createNotification(Context context) {

        Intent intent = new Intent(context, FraudUserAlert.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("Fraud Call Alert.")
                .setContentText("Sensitive Information Asked.").setSmallIcon(R.drawable.baware_icon)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);

    }

}
