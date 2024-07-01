package com.example.study.binderDemo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;


import java.util.ArrayList;
import java.util.List;

public class UserService extends Service {
    private ArrayList<User> userArrayList = new ArrayList<>();

    public UserService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userArrayList.add(new User("zhang", 18));
        userArrayList.add(new User("li", 18));
    }

    private Binder binder = new IUserManager.Stub() {
        @Override
        public List<User> getUser() throws RemoteException {
            return userArrayList;
        }

        @Override
        public void addUser(User user) throws RemoteException {
            userArrayList.add(user);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}