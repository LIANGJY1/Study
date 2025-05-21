package com.example.study;

import android.app.Application;

import com.example.study.greendao.DBManager;
import com.liang.log.MLog;

import java.io.File;

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
        Constant.DOWNLOAD_DIR = getFilesDir() + "/Download/";
        final File downloadDir = new File(Constant.DOWNLOAD_DIR);
        MLog.i("DOWNLOAD_DIR: " + Constant.DOWNLOAD_DIR);
        downloadDir.mkdirs();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: 耗时
                DBManager.getInstance().initDBManager(mInstance);
            }
        }).start();
    }
}
