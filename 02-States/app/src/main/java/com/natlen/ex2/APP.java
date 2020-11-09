package com.natlen.ex2;

import android.app.Application;
import android.util.Log;

public class APP extends Application {
    public static final String AppName = "ex2 - Simple Calculator";

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(AppName, "APP: onCreate()");
    }
}
