package com.example.b_aware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.Manifest;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        /*  */

        final Intent intent = new Intent(this.getApplicationContext(), IListeningService2.class);


        final EditText editText = findViewById(R.id.editText);
//        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        findViewById(R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // stopService(intent);
//                        mSpeechRecognizer.stopListening();
//                        editText.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        startService(intent);
//                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
//                        editText.setText("");
//                        editText.setHint("Listening...");
                        break;
                }
                return false;
            }
        });


//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
//                Locale.getDefault());
//
//        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
//            @Override
//            public void onReadyForSpeech(Bundle bundle) {
//
//            }
//
//            @Override
//            public void onBeginningOfSpeech() {
//
//            }
//
//            @Override
//            public void onRmsChanged(float v) {
//
//            }
//
//            @Override
//            public void onBufferReceived(byte[] bytes) {
//
//            }
//
//            @Override
//            public void onEndOfSpeech() {
//
//            }
//
//            @Override
//            public void onError(int i) {
//
//            }
//
//            @Override
//            public void onResults(Bundle bundle) {
//                //getting all the matches
//                ArrayList<String> matches = bundle
//                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//
//                //displaying the first match
//                if (matches != null)
//                    editText.setText(matches.get(0));
//            }
//
//            @Override
//            public void onPartialResults(Bundle bundle) {
//                //ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                //ArrayList<String> unstableData = bundle.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
//                //String mResult = data.get(0) + unstableData.get(0);
//            }
//
//            @Override
//            public void onEvent(int i, Bundle bundle) {
//
//            }
//        });


    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}
