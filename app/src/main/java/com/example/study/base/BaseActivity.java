package com.example.study.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.example.study.util.tool.PackageUtil;
import com.liang.log.MLog;

public abstract class BaseActivity<P extends BasePresenter, VB extends ViewBinding> extends AppCompatActivity {
    protected String TAG;
    private VB binding;
    protected P mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = PackageUtil.getSimpleClassName(this);
        printLog("onCreate");
        binding = getViewBinding();
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void printLog(String methodName) {
        if (TAG.equals("FragmentTestActivity")) {
            MLog.i(TAG + ", " + methodName);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        printLog("onResume");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        printLog("onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        printLog("onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        printLog("onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        printLog("onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        printLog("onPause");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        printLog("onNewIntent");
    }
}
