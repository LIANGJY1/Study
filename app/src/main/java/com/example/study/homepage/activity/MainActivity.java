package com.example.study.homepage.activity;

import android.content.Intent;
import android.util.Log;

import com.example.study.base.BaseActivity;
import com.example.study.binderDemo.BinderDemoActivity;
import com.example.study.databinding.ActivityMainBinding;
import com.example.study.homepage.presenter.MainPresenter;
import com.example.study.javaDemo.JavaDemoActivity;
import com.example.study.viewDemo.ViewDemoActivity;

public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
//        getViewBound().btView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ViewDemoActivity.class)));
        getViewBound().btView.setOnClickListener(v -> test());
        getViewBound().btJava.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, JavaDemoActivity.class)));
        getViewBound().btAidl.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BinderDemoActivity.class)));
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
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
}