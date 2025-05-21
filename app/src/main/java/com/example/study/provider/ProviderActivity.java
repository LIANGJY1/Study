package com.example.study.provider;

import androidx.viewbinding.ViewBinding;

import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityProviderBinding;

public class ProviderActivity extends BaseActivity<ProviderPresenter, ActivityProviderBinding> {

    @Override
    protected ProviderPresenter onLoadPresenter() {
        return new ProviderPresenter();
    }

    @Override
    protected ActivityProviderBinding getViewBinding() {
        return ActivityProviderBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
    }


}