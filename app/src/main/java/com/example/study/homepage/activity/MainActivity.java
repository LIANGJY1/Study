package com.example.study.homepage.activity;

import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityMainBinding;
import com.example.study.homepage.presenter.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
    }

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
}