package com.example.study.javaDemo.dataSerializationDemo.activity;

import android.content.Intent;

import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityDataSerializationDemoBinding;

public class DataSerializationDemoActivity extends BaseActivity<BasePresenter, ActivityDataSerializationDemoBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().btSerialization.setOnClickListener(v -> startActivity(new Intent(DataSerializationDemoActivity.this, SerializableDemoActivity.class)));
        getViewBound().btExternalizable.setOnClickListener(v -> startActivity(new Intent(DataSerializationDemoActivity.this, ExternalizableDemoActivity.class)));
        getViewBound().btParcelable.setOnClickListener(v -> startActivity(new Intent(DataSerializationDemoActivity.this, ParcelableDemoActivity.class)));
    }

    @Override
    protected BasePresenter onLoadPresenter() {
        return new BasePresenter();
    }

    @Override
    protected ActivityDataSerializationDemoBinding getViewBinding() {
        return ActivityDataSerializationDemoBinding.inflate(getLayoutInflater());
    }
}