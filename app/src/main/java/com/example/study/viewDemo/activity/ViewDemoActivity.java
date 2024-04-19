package com.example.study.viewDemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.audiofx.BassBoost;
import android.os.Bundle;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityViewDemoBinding;
import com.example.study.viewDemo.presenter.ViewDemoPresenter;

public class ViewDemoActivity extends BaseActivity<ViewDemoPresenter, ActivityViewDemoBinding> {

    @Override
    protected ViewDemoPresenter onLoadPresenter() {
        return new ViewDemoPresenter();
    }

    @Override
    protected ActivityViewDemoBinding getViewBinding() {
        return ActivityViewDemoBinding.inflate(getLayoutInflater());
    }
}