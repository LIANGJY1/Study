package com.example.study.viewDemo;

import android.content.Intent;

import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityViewDemoBinding;
import com.example.study.viewDemo.LayoutInflaterDemo.activity.LayoutInflaterDemoActivity;
import com.example.study.viewDemo.viewSystem.activity.ScrollViewActivity;
import com.example.study.viewDemo.viewSystem.activity.ViewClickDemoActivity;
import com.example.study.viewDemo.viewSystem.activity.ViewParamsDemoActivity;

public class ViewDemoActivity extends BaseActivity<ViewDemoPresenter, ActivityViewDemoBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().btLayoutInflater.setOnClickListener(v -> startActivity(new Intent(ViewDemoActivity.this, LayoutInflaterDemoActivity.class)));
        getViewBound().btClick.setOnClickListener(v -> startActivity(new Intent(ViewDemoActivity.this, ViewClickDemoActivity.class)));
        getViewBound().btParams.setOnClickListener(v -> startActivity(new Intent(ViewDemoActivity.this, ViewParamsDemoActivity.class)));
        getViewBound().btScrollView.setOnClickListener(v -> startActivity(new Intent(ViewDemoActivity.this, ScrollViewActivity.class)));
    }

    @Override
    protected ViewDemoPresenter onLoadPresenter() {
        return new ViewDemoPresenter();
    }

    @Override
    protected ActivityViewDemoBinding getViewBinding() {
        return ActivityViewDemoBinding.inflate(getLayoutInflater());
    }
}