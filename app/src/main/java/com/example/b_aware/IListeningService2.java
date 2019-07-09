package com.example.b_aware;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Locale;

public class IListeningService2 extends Service {
    final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    // final TelephonyManager telephonyManager;
    boolean isListening = false;

    public IListeningService2() {
        // telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, Locale.getDefault());

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
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                ArrayList<String> unstableData = bundle.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
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
            mSpeechRecognizer.stopListening();
            Log.i("TAG", "Stopped Service");
        }
        return  START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
