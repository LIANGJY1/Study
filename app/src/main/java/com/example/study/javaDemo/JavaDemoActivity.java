package com.example.study.javaDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityJavaDemoBinding;
import com.example.study.javaDemo.dataSerializationDemo.activity.DataSerializationDemoActivity;

public class JavaDemoActivity extends BaseActivity<BasePresenter, ActivityJavaDemoBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().btSerialization.setOnClickListener(v -> startActivity(new Intent(JavaDemoActivity.this, DataSerializationDemoActivity.class)));
    }

    @Override
    protected BasePresenter onLoadPresenter() {
        return new BasePresenter();
    }

    @Override
    protected ActivityJavaDemoBinding getViewBinding() {
        return ActivityJavaDemoBinding.inflate(getLayoutInflater());
    }
}