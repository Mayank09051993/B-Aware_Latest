package com.example.b_aware;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity currentActivty  = this;

        /*  */

        final Intent intent = new Intent(this.getApplicationContext(), ListeningService.class);
        final Switch aSwitch = findViewById(R.id.ProtectMe);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(PermissionHandler.checkPermission(currentActivty, PermissionHandler.RECORD_AUDIO)){
                    if(isChecked){
                        startService(intent);
                    }
                    else{
                        //stopService(intent);
                    }
                }
                else{
                    PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO, currentActivty);
                }
            }
        });
    }
}
