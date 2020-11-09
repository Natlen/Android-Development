package com.rkcodesolution.lab09;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int SMS_PERMISSION = 1234;
    private TextView mSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSms = findViewById(R.id.sms);

        handleSmsPermission();
    }

    /**
     * Request SMS Receive permission.
     * @return true if granted.
     */
    private void handleSmsPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted. show an explanation.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {

                Toast.makeText(this, R.string.sms, Toast.LENGTH_LONG).show();
            } else {
                // request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        SMS_PERMISSION);
            }
            mSms.setEnabled(false);
            mSms.setBackgroundColor(ContextCompat.getColor(this, R.color.disabled));
        } else {
            // Permission has already been granted, register sms receiver.
            registerSms();
        }
    }

    private void registerSms() {
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(new SmsBroadcastReceiver(mSms), filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    registerSms();
                } else {
                    mSms.setEnabled(false);
                    mSms.setBackgroundColor(ContextCompat.getColor(this, R.color.disabled));
                }
            }
        }
    }
}
