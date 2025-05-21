package com.example.study.pms;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityPmsactivityBinding;
import com.example.studysdk.util.ApkUtil;
import com.liang.log.MLog;

public class PMSActivity extends BaseActivity<PMSPresenter, ActivityPmsactivityBinding> {

    @Override
    protected PMSPresenter onLoadPresenter() {
        return new PMSPresenter();
    }

    @Override
    protected ActivityPmsactivityBinding getViewBinding() {
        return ActivityPmsactivityBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().install.setOnClickListener(v -> mPresenter.install(PMSActivity.this));
    }
}