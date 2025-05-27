package com.example.study.fragment;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityFragmentTestBinding;

public class FragmentTestActivity extends BaseActivity<FragmentTestPresenter, ActivityFragmentTestBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
    }

    @Override
    protected FragmentTestPresenter onLoadPresenter() {
        return new FragmentTestPresenter();
    }

    @Override
    protected ActivityFragmentTestBinding getViewBinding() {
        return ActivityFragmentTestBinding.inflate(getLayoutInflater());
    }
}