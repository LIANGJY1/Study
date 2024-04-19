package com.example.study.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<P extends BasePresenter, VB extends ViewBinding> extends AppCompatActivity {
    private VB binding;
    private P mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        beforeSetContentView();
        setContentView(binding .getRoot());
        mPresenter = onLoadPresenter();
        afterSetContentView();
    }

    protected abstract P onLoadPresenter();

    protected abstract VB getViewBinding();

    protected VB getViewBound() {
        return binding;
    }

    protected void afterSetContentView() {
    }

    protected void beforeSetContentView() {
    }

    @Override
    public void finish() {
        super.finish();
        binding = null;
    }
}
