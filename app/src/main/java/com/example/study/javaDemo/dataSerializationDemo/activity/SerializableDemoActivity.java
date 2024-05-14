package com.example.study.javaDemo.dataSerializationDemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivitySerializableDemoBinding;
import com.example.study.javaDemo.dataSerializationDemo.bean.Student;
import com.example.study.javaDemo.dataSerializationDemo.presenter.SerializableDemoPresenter;

import java.io.File;
import java.util.Objects;

public class SerializableDemoActivity extends BaseActivity<SerializableDemoPresenter, ActivitySerializableDemoBinding> {
    String externalFilesDirString;
    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
        externalFilesDirString = Objects.requireNonNull(this.getExternalFilesDir(null)).getAbsolutePath();
        Log.i("SerializableDemoActivity  getAbsolutePath", externalFilesDirString);
    }

    private void initializeActivity() {
       getViewBound().btSave.setOnClickListener(v -> mPresenter.saveObject(new Student("li", 18, 1), externalFilesDirString));
       getViewBound().btRead.setOnClickListener(v -> {
           Student student = mPresenter.readObject(externalFilesDirString);
           Log.i("SerializableDemoActivity", "name is " + student.getName() + "; age is " + student.getAge());
       });
    }

    @Override
    protected SerializableDemoPresenter onLoadPresenter() {
        return new SerializableDemoPresenter();
    }

    @Override
    protected ActivitySerializableDemoBinding getViewBinding() {
        return ActivitySerializableDemoBinding.inflate(getLayoutInflater());
    }
}