package com.example.study;

import android.app.Application;

import com.liang.log.MLog;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MLog.init(this);
//        for (int i = 0; i < 1000000; i++) {
//            MLog.i("111111");
//        }
    }
}
