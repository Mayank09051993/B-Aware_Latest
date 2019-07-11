package com.example.b_aware;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.Locale;

public class IListeningService2 extends Service {
    final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    boolean isListening = false;

    public IListeningService2() {

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    Log.i("TAG",matches.get(0));

                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                ArrayList<String> unstableData = bundle.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
                if((data.get(0) + unstableData.get(0)).toUpperCase().contains("CARD") ||
                        (data.get(0) + unstableData.get(0)).toUpperCase().contains("OTP") ||
                        (data.get(0) + unstableData.get(0)).toUpperCase().contains("CVV"))
                {
                    Log.i("Sensitive Information", "Sensitive Information Asked...!!!");
                    createNotification(null);
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.EFFECT_HEAVY_CLICK));
                    }
                }

                Log.i("partial", data.get(0) + unstableData.get(0));
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    private long startTime;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TAG", "Service is running");
        if (!isListening){
            startTime = System.nanoTime();
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            isListening = true;
            Log.i("TAG", "Started Listening");
        }
        else if(isListening && (System.nanoTime() - startTime) > Long.parseLong("1000000000")){
            Log.i("TAG", "Stopped Service");
            mSpeechRecognizer.stopListening();
        }
        return  START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void createNotification(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Fraud Call Alert.")
                .setContentText("Sensitive Information Asked.").setSmallIcon(R.drawable.baware_icon)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);

    }
}
