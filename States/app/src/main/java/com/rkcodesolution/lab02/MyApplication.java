package com.rkcodesolution.lab02;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    public static final String MYAPP = "Lab02";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(MYAPP, "MyApplication: onCreate()");
    }
}
