package com.ghostdetctor.ghost_detector;

import android.app.Application;

import com.ghostdetctor.ghost_detector.util.SharePrefUtils;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

