package com.example.study.javaDemo.dataSerializationDemo.activity;

import android.util.Log;

import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityParcelableDemoBinding;
import com.example.study.javaDemo.dataSerializationDemo.bean.Leader;
import com.example.study.javaDemo.dataSerializationDemo.presenter.ParcelableDemoPresenter;

import java.util.Objects;

public class ParcelableDemoActivity extends BaseActivity<ParcelableDemoPresenter, ActivityParcelableDemoBinding> {
    String externalFilesDirString;
    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        externalFilesDirString = Objects.requireNonNull(this.getExternalFilesDir(null)).getAbsolutePath();
        initializeActivity();
        Log.i("SerializableDemoActivity  getAbsolutePath", externalFilesDirString);
    }

    private void initializeActivity() {
        getViewBound().btSave.setOnClickListener(v -> mPresenter.saveObject(new Leader("wang", 18), externalFilesDirString));
        getViewBound().btRead.setOnClickListener(v -> {
            Leader leader = mPresenter.readObject(externalFilesDirString);
            Log.i("SerializableDemoActivity", "name is " + leader.getName() + "; age is " + leader.getAge());
        });
    }

    @Override
    protected ParcelableDemoPresenter onLoadPresenter() {
        return new ParcelableDemoPresenter();
    }

    @Override
    protected ActivityParcelableDemoBinding getViewBinding() {
        return ActivityParcelableDemoBinding.inflate(getLayoutInflater());
    }
}