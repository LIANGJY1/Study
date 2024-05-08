package com.example.study.viewDemo.viewSystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityViewClickDemoBinding;
import com.example.study.viewDemo.viewSystem.presenter.ViewClickDemoPresenter;

public class ViewClickDemoActivity extends BaseActivity<ViewClickDemoPresenter, ActivityViewClickDemoBinding> {

    @Override
    protected ViewClickDemoPresenter onLoadPresenter() {
        return new ViewClickDemoPresenter();
    }

    @Override
    protected ActivityViewClickDemoBinding getViewBinding() {
        return ActivityViewClickDemoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().btClickThrough.setOnClickListener( v -> startActivity(new Intent(ViewClickDemoActivity.this, ClickThroughActivity.class)));
    }

    private void clickThroughTest() {

    }
}