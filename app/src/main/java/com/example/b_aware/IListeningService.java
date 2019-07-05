package com.example.b_aware;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(file.getAbsolutePath() + "/" + "Mayank_rec.3gp");
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(new PhoneStateListener()
            {
                @Override
                public void onCallStateChanged(int state, String incomingPhoneNumber)
                {
                    if (state == TelephonyManager.CALL_STATE_IDLE && mediaRecorder == null) {
                        mediaRecorder.stop();
                        mediaRecorder.reset();
                        mediaRecorder.release();
                        recordedState = false;
                        stopSelf();
                    } else if (state == TelephonyManager.CALL_STATE_RINGING || state == TelephonyManager.CALL_STATE_OFFHOOK)
                    {
                        try
                        {
                            mediaRecorder.prepare();
                        }
                        catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }
                        mediaRecorder.start();
                        recordedState = true;
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
}
