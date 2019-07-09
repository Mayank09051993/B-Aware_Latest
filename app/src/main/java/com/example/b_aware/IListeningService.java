package com.example.b_aware;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;

import androidx.annotation.Nullable;

import java.io.IOException;

public class IListeningService extends Service {
    public IListeningService() {
    }

    private MediaRecorder mediaRecorder;
    private boolean recordedState;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //onTaskRemoved(intent);
        /*
        What To Do When Service Is Called.
         */
        try
        {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(file.getAbsolutePath() + "/Mayank_karnama.3gp");
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
                public void onError(MediaRecorder arg0, int arg1, int arg2) {
                    Log.e("TAG", "OnErrorListener " + arg1 + "," + arg2);
                    //terminateAndEraseFile();
                }
            };
            mediaRecorder.setOnErrorListener(errorListener);
            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(new PhoneStateListener()
            {
                @Override
                public void onCallStateChanged(int state, String incomingPhoneNumber)
                {
                    if (state == TelephonyManager.CALL_STATE_IDLE) {
                        if(recordedState == true)
                        {
                            mediaRecorder.stop();
                            mediaRecorder.reset();
                            mediaRecorder.release();
                            recordedState = false;
                            stopSelf();
                        }

                    } else if (state == TelephonyManager.CALL_STATE_OFFHOOK)
                    {
                        try
                        {
                            mediaRecorder.prepare();
                            SleepThread();

                        mediaRecorder.start();
                        recordedState = true;
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }
        catch (IllegalStateException ex)
        {
            ex.printStackTrace();
        }
        return START_STICKY;
    }

    private void SleepThread() throws InterruptedException {
        Thread.sleep(5000);
    }
}
