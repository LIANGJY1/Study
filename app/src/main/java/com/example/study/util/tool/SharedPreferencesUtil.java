package com.example.study.util.tool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.example.studysdk.util.DataUtil;
import com.liang.log.MLog;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;

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
//        MLog.i(TAG + sharedPreferences.toString());
////        MLog.i(TAG + context.getLocalClassName());
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        File dataDir = context.getDataDir();
        MLog.i(TAG + "getDataDir: " + context.getDataDir());
//        MLog.i(TAG + "getCacheDir: " + deviceContext.getCacheDir());
//        MLog.i(TAG + "getFilesDir: " + deviceContext.getFilesDir());
//        MLog.i(TAG + "getExternalCacheDir: " + deviceContext.getExternalCacheDir());
//        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();

        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
                MLog.i(TAG + "onSharedPreferenceChanged, key: " + key);
            }
        });


        sharedPreferences.edit()
                .putBoolean("isDarkMode", true)
                .putInt("userAge", 25)
                .putFloat("rating", 4.5f)
                .putLong("lastLogin", System.currentTimeMillis())
                .putString("username", "")
                .putStringSet("tags", new HashSet<>(Arrays.asList("sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music","sports", "music", "sports", "music", "sports", "music","sports", "music")))
                .apply(); // 异步提交（推荐常规使用）

        MLog.i(TAG + "username" + sharedPreferences.getString("username", "1"));


        int a = 2560;
        int b = 1440;
        double c = Math.sqrt(a*a + b*b)/15.6;
        MLog.i(TAG + "c: " + c);
        SharedPreferences.Editor edit = sharedPreferences.edit();
//        for (int i = 0; i < 1000000; i++) {
//            edit.putInt(DataUtil.generate(9), 1);
//        }
//        edit.commit();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                edit.commit();
//            }
//        }).start();
//
//        edit.commit();
        boolean commit = edit.commit();
        MLog.i(TAG + "success");
    }


    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

}
