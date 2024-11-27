package com.example.study.viewDemo.viewSystem.activity;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewbinding.ViewBinding;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityScrollViewBinding;

public class ScrollViewActivity extends BaseActivity<BasePresenter, ActivityScrollViewBinding> {

    @Override
    protected BasePresenter onLoadPresenter() {
        return null;
    }

    @Override
    protected ActivityScrollViewBinding getViewBinding() {
        return ActivityScrollViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeView();
    }

    @SuppressLint("ResourceAsColor")
    private void initializeView() {
        TextView textView = new TextView(this);
//        textView.set
        textView.setWidth(500);
        textView.setHeight(500);
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textView.getLayoutParams();
        // 初始化LayoutParams，因为新创建的TextView还没有LayoutParams
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getViewBound().main.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;

        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(R.color.black);

        getViewBound().getRoot().addView(textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "x：" + textView.getX()+"；y："+textView.getY());
            }
        });
    }
}