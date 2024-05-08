package com.example.study.viewDemo.viewSystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityClickThroughBinding;

public class ClickThroughActivity extends BaseActivity<BasePresenter, ActivityClickThroughBinding> {

    @Override
    protected BasePresenter onLoadPresenter() {
        return new BasePresenter();
    }

    @Override
    protected ActivityClickThroughBinding getViewBinding() {
        return ActivityClickThroughBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().view1.setOnClickListener(v -> {
            Log.i(TAG, "this is view1");
        });
        getViewBound().view2.setOnClickListener(v -> {
            getViewBound().view1.setEnabled(false);
//            getViewBound().view2.setClickable(true);
//            getViewBound().view2.setFocusable(true);
            Log.i(TAG, "this is view2");
        });
    }
}