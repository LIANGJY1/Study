package com.example.study.viewDemo.viewSystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityViewParamsDemoBinding;
import com.example.study.viewDemo.viewSystem.presenter.ViewParamsDemoPresenter;

public class ViewParamsDemoActivity extends BaseActivity<ViewParamsDemoPresenter, ActivityViewParamsDemoBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().test.setOnClickListener(v -> mPresenter.getViewWidthAndHeight(getViewBound().vBlack));
    }

    @Override
    protected ViewParamsDemoPresenter onLoadPresenter() {
        return new ViewParamsDemoPresenter();
    }

    @Override
    protected ActivityViewParamsDemoBinding getViewBinding() {
        return ActivityViewParamsDemoBinding.inflate(getLayoutInflater());
    }

}