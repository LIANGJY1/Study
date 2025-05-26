package com.example.study.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityServiceTestBinding;
import com.liang.log.MLog;

import java.util.List;

public class ServiceTestActivity extends BaseActivity<BasePresenter, ActivityServiceTestBinding> {

    @Override
    protected BasePresenter onLoadPresenter() {
        return null;
    }

    @Override
    protected ActivityServiceTestBinding getViewBinding() {
        return ActivityServiceTestBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeView();
    }

    private void initializeView() {
//        getViewBound().btStartService.setOnClickListener(v -> startService(new Intent(ServiceTestActivity.this, MyLocalService.class)));
        getViewBound().btStartService.setOnClickListener(v -> bindService(new Intent(ServiceTestActivity.this, MyUserService.class), serviceConnection, Context.BIND_AUTO_CREATE));
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MLog.i("MyUserService onServiceConnected");
            IUserManager iUserManager = IUserManager.Stub.asInterface(service);
            try {
                iUserManager.addUser(new User("wang",8433));
                List<User> users = iUserManager.getUser();
                users.forEach(user -> MLog.i("MyUserService users: " + user.getName()));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}