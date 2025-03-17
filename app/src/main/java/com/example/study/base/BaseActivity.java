package com.example.study.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.example.study.util.tool.PackageUtil;

public abstract class BaseActivity<P extends BasePresenter, VB extends ViewBinding> extends AppCompatActivity {
    protected String TAG;
    private VB binding;
    protected P mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        TAG = PackageUtil.getSimpleClassName(this);
        beforeSetContentView();
        setContentView(binding.getRoot());
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

//    @Override
//    public void finish() {
//        super.finish();
//        binding = null;
//    }
}
