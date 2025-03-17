package com.example.study.activityTest;

import android.content.Intent;

import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityMainBinding;
import com.example.study.databinding.ActivityTestBinding;

public class ActivityTestActivity extends BaseActivity<BasePresenter, ActivityTestBinding> {


    @Override
    protected BasePresenter onLoadPresenter() {
        return null;
    }

    @Override
    protected ActivityTestBinding getViewBinding() {
        return ActivityTestBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        getViewBound().btActivityLifecycle.setOnClickListener(v -> startActivity(new Intent(ActivityTestActivity.this, ALifecycleActivity.class)));
    }
}