package com.example.study.activityTest;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityAlifecycleBinding;
import com.example.study.util.tool.LogUtil;
import com.liang.log.MLog;

public class ALifecycleActivity extends BaseActivity<BasePresenter, ActivityAlifecycleBinding> {

    @Override
    protected BasePresenter onLoadPresenter() {
        return null;
    }

    @Override
    protected ActivityAlifecycleBinding getViewBinding() {
        return ActivityAlifecycleBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLog.i(LogUtil.getCurrentMethodName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MLog.i(LogUtil.getCurrentMethodName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MLog.i(LogUtil.getCurrentMethodName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        MLog.i(LogUtil.getCurrentMethodName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.i(LogUtil.getCurrentMethodName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.i(LogUtil.getCurrentMethodName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        MLog.i(LogUtil.getCurrentMethodName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLog.i(LogUtil.getCurrentMethodName());
    }
}