package com.example.study.homepage.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.study.base.BaseActivity;
import com.example.study.binderDemo.BinderDemoActivity;
import com.example.study.broadcast.BroadcastActivity;
import com.example.study.databinding.ActivityMainBinding;
import com.example.study.homepage.presenter.MainPresenter;
import com.example.study.javaDemo.JavaDemoActivity;
import com.example.study.notification.NotificationActivity;
import com.example.study.viewDemo.ViewDemoActivity;

public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> {

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String language = newConfig.getLocales().get(0).getLanguage();
        Log.i("MainActivity","language ———— " + language);// language ———— zh
        updateUI();

        // 检查相机权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求相机权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            // 权限已经授予，启动服务
        }
    }

    private void updateUI() {
    }

    private void initializeActivity() {
        getViewBound().btView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ViewDemoActivity.class)));
//        getViewBound().btView.setOnClickListener(v -> test());
//        getViewBound().btView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
//            }
//        });
        getViewBound().btJava.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, JavaDemoActivity.class)));
        getViewBound().btAidl.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BinderDemoActivity.class)));
        getViewBound().btNotification.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NotificationActivity.class)));
        getViewBound().btBroadcast.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BroadcastActivity.class)));
    }

    private void test() {
        int i = 3;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("this", "i = " + i);
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，启动服务
            } else {
                // 权限被拒绝
                // 可以向用户显示一条消息，解释为什么需要这个权限
            }
        }
    }

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
}