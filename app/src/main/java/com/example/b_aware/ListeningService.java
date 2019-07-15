package com.example.b_aware;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class ListeningService extends Service {

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private boolean startAnalyzing = false;
    private RiskCalculationEngine riskCalculationEngine;
    private FraudUserAlert fraudUserAlert;
    private UUID riskCalculationSessionId;
    private double FRAUD_CALL_SCORE_LIMIT = 10.0;

    public ListeningService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        try
        {
            Log.v("ListeningService", "onStartCommand");

            setupSpeechRecognition();

            riskCalculationEngine = new RiskCalculationEngine(getApplicationContext());
            fraudUserAlert = new FraudUserAlert(getApplicationContext());

            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(new PhoneStateListener()
            {
                @Override
                public void onCallStateChanged(int state, String incomingPhoneNumber)
                {

                    if (state == TelephonyManager.CALL_STATE_OFFHOOK)
                    {
                        riskCalculationSessionId = riskCalculationEngine.startSession();
                        startAnalyzing = true;
                    }
                    else
                    {
                        startAnalyzing = false;
                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            startAnalyzing = false;
        }
        return START_STICKY;
    }

    public void setupSpeechRecognition() {

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());

        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
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
                speechRecognizer.startListening(speechRecognizerIntent);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

                if(startAnalyzing){
                    ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    ArrayList<String> unstableData = bundle.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
                    Log.i("Partial",data.get(0) + unstableData.get(0));
                    double score = riskCalculationEngine.calculateRisk(riskCalculationSessionId, data.get(0) + unstableData.get(0));
                    Log.i("Score",Double.toString(score));
                    if(score > FRAUD_CALL_SCORE_LIMIT){
                        fraudUserAlert.alertUser();
                        startAnalyzing = false;
                    }
                }
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        speechRecognizer.startListening(speechRecognizerIntent);
    }
}
