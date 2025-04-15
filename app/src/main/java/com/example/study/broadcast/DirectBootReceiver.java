package com.example.study.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.liang.log.MLog;

public class DirectBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // 使用默认上下文（CE存储）
            SharedPreferences prefs = context.getSharedPreferences(
                    "secure_prefs", Context.MODE_PRIVATE);

            // 尝试读取数据（此时会触发异常）
            String value = prefs.getString("key", "default");
            MLog.i("DirectBoot", "Value: " + value);
        } catch (IllegalStateException e) {
            MLog.i("DirectBoot", "Exception: ", e);
            throw e; // 抛出目标异常
        }
    }
}
