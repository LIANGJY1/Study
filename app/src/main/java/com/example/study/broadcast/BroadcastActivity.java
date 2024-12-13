package com.example.study.broadcast;


import android.content.IntentFilter;
import android.util.Log;

import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityBroadcastBinding;

public class BroadcastActivity extends BaseActivity<BasePresenter, ActivityBroadcastBinding> {
    private  NetWorkReceiver networkReceiver;

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        Log.i("NetWorkReceiver", "main thread");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkReceiver = new NetWorkReceiver();
//        registerReceiver(networkReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(networkReceiver);
    }

    @Override
    protected BasePresenter onLoadPresenter() {
        return null;
    }

    @Override
    protected ActivityBroadcastBinding getViewBinding() {
        return ActivityBroadcastBinding.inflate(getLayoutInflater());
    }
}