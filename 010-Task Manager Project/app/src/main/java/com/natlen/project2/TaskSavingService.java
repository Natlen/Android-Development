package com.natlen.project2;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import static com.natlen.project2.TaskSavingServiceController.CHANNEL_ID;

public class TaskSavingService extends Service {


    public TaskSavingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Permission has already been granted, register sms receiver.
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(new SMSBroadcastReceiver(), filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification noty = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.service_icon)
                .setContentTitle("Task Saving Service")
                .setContentText("Awaiting a task to be received via SMS")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, noty);
        return super.onStartCommand(intent, flags, startId);
    }
}
