package com.example.study.util.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Build;

import com.liang.log.MLog;

import java.io.File;

public class SharedPreferencesUtil {
    private String TAG = "SharedPreferencesUtil, ";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final SharedPreferencesUtil INSTANCE = new SharedPreferencesUtil();

    private SharedPreferencesUtil() {

    }

    public static SharedPreferencesUtil getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {

//        Context deviceContext = context.createDeviceProtectedStorageContext();
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        File dataDir = context.getDataDir();
//        MLog.i(TAG + "getDataDir: " + deviceContext.getDataDir());
//        MLog.i(TAG + "getCacheDir: " + deviceContext.getCacheDir());
//        MLog.i(TAG + "getFilesDir: " + deviceContext.getFilesDir());
//        MLog.i(TAG + "getExternalCacheDir: " + deviceContext.getExternalCacheDir());

    }


    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

}
