package com.rkcodesolution.lab09;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.TextView;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private TextView mSmsTextView;

    public SmsBroadcastReceiver(TextView smsTextView) {
        mSmsTextView = smsTextView;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            SmsMessage smsMessage = msgs[0];

            if (smsMessage != null) {
                String sender = smsMessage.getDisplayOriginatingAddress();
                String smsChunk = smsMessage.getDisplayMessageBody();
                String data = "From: " + sender + "\n\nText Received: \n" + smsChunk;
                mSmsTextView.setText(data);
            }
        }
    }
}
