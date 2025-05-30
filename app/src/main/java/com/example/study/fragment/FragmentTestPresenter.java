package com.example.study.fragment;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.study.R;
import com.example.study.base.BasePresenter;

public class FragmentTestPresenter extends BasePresenter {
    public void createFragment() {
    }

    public void replaceFragment(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();// 获取 FragmentManager
        FragmentTransaction transaction = fragmentManager.beginTransaction();// 开启一个事务 beginTransaction()
        transaction.replace(R.id.fragment_layout, new LeftFragment());
        transaction.commit();
    }
}
