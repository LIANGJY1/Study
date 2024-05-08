package com.example.study.homepage.activity;

import android.content.Intent;

import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityMainBinding;
import com.example.study.homepage.presenter.MainPresenter;
import com.example.study.viewDemo.ViewDemoActivity;

public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().btView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ViewDemoActivity.class)));
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