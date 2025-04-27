package com.example.study;

import android.app.Application;

import com.example.study.greendao.DBManager;
import com.liang.log.MLog;

public class App extends Application {

    private App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MLog.init(this);
//        for (int i = 0; i < 1000000; i++) {
//            MLog.i("111111");
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: 耗时
                DBManager.getInstance().initDBManager(mInstance);
            }
        }).start();
    }
}
