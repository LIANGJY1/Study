package com.example.study.homepage.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.study.activityTest.ActivityTestActivity;
import com.example.study.base.BaseActivity;
import com.example.study.binderDemo.BinderDemoActivity;
import com.example.study.broadcast.BroadcastActivity;
import com.example.study.databinding.ActivityMainBinding;
import com.example.study.homepage.presenter.MainPresenter;
import com.example.study.javaDemo.JavaDemoActivity;
import com.example.study.notification.NotificationActivity;
import com.example.study.pattern.singleton.Person;
import com.example.study.service.ServiceTestActivity;
import com.example.study.util.tool.LogUtil;
import com.example.study.util.tool.SharedPreferencesUtil;
import com.example.study.viewDemo.ViewDemoActivity;
import com.example.studysdk.util.ApkUtil;
import com.example.studysdk.util.RxJavaUtil;
import com.liang.log.MLog;

import org.json.JSONException;

public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> {

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
        RxJavaUtil.test2();
//        ApkUtil.test(MainActivity.this);
        ApkUtil.test2();
//        SharedPreferencesUtil.getInstance().init(this);
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
        getViewBound().btActivity.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ActivityTestActivity.class)));
        getViewBound().btService.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ServiceTestActivity.class)));
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Person.run();
//        MLog.i(LogUtil.getCurrentMethodName());
//        Book book1 = new Book("111");
//        Book book2 = book1;
//        rename(book1);
//        MLog.i(book1.getName());
    }

    private void rename(Book book1) {
        Book book2 = book1;
        book2.setName("222");
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        MLog.i(LogUtil.getCurrentMethodName());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        MLog.i(LogUtil.getCurrentMethodName());
    }
}