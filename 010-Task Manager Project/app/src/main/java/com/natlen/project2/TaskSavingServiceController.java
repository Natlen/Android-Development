package com.natlen.project2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class TaskSavingServiceController extends Application {
    public static final String CHANNEL_ID = "TaskSavingService";

    /* ---- registered on Manifest ---- */

    @Override
    public void onCreate() {
        super.onCreate();
        establishNotificationChannel();
    }
    private void establishNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Task Saving Service", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }
}
