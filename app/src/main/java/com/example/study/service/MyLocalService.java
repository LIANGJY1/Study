package com.example.study.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.liang.log.MLog;

public class MyLocalService extends Service {
    public MyLocalService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        stopSelf();
        MLog.i("service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MLog.i("service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        MLog.i("service onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return new Binder();
    }
}