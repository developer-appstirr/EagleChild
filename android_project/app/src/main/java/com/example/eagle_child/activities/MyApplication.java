package com.example.eagle_child.activities;


import android.app.Application;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initPython();
    }

    private void initPython() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
    }
}
