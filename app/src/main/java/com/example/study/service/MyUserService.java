package com.example.study.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyUserService extends Service {
    private ArrayList<User> users = new ArrayList<>();
    public MyUserService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        users.add(new User("adf", 1));
        users.add(new User("rdsgs", 3));
    }

    private Binder binder = new IUserManager.Stub() {
        @Override
        public List<User> getUser() throws RemoteException {
            return users;
        }

        @Override
        public void addUser(User user) throws RemoteException {
            users.add(user);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}