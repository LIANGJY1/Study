package com.example.study.viewDemo.LayoutInflaterDemo.activity;

import android.util.Log;
import android.view.LayoutInflater;

import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityLayoutInflaterDemoBinding;
import com.example.study.viewDemo.LayoutInflaterDemo.presenter.LayoutInflaterDemoPresenter;

public class LayoutInflaterDemoActivity extends BaseActivity<LayoutInflaterDemoPresenter, ActivityLayoutInflaterDemoBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().btCreateLayoutInflater.setOnClickListener(v -> createLayoutInflaterWithFrom());
    }

    private void createLayoutInflaterWithFrom() {
        LayoutInflater activityLayoutInflater1 = LayoutInflater.from(LayoutInflaterDemoActivity.this);
        LayoutInflater activityLayoutInflater2 = LayoutInflater.from(LayoutInflaterDemoActivity.this);
        LayoutInflater appLayoutInflater = LayoutInflater.from(getApplicationContext());
        Log.i(TAG, "activityLayoutInflater1: "+ activityLayoutInflater1 + ";  activityLayoutInflater2: "+ activityLayoutInflater2 + ";  appLayoutInflater: " + appLayoutInflater);
    }

    @Override
    protected LayoutInflaterDemoPresenter onLoadPresenter() {
        return new LayoutInflaterDemoPresenter();
    }

    @Override
    protected ActivityLayoutInflaterDemoBinding getViewBinding() {
        return ActivityLayoutInflaterDemoBinding.inflate(getLayoutInflater());
    }
}