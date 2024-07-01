package com.example.study.binderDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.binderDemo.aidl.AIDLDemoActivity;
import com.example.study.databinding.ActivityBinderDemoBinding;

public class BinderDemoActivity extends BaseActivity<BasePresenter, ActivityBinderDemoBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().btAidl.setOnClickListener(v -> startActivity(new Intent(BinderDemoActivity.this, AIDLDemoActivity.class)));
    }

    @Override
    protected BasePresenter onLoadPresenter() {
        return new BasePresenter();
    }

    @Override
    protected ActivityBinderDemoBinding getViewBinding() {
        return ActivityBinderDemoBinding.inflate(getLayoutInflater());
    }
}