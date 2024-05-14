package com.example.study.javaDemo.dataSerializationDemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityExternalizableBinding;
import com.example.study.javaDemo.dataSerializationDemo.bean.Teacher;
import com.example.study.javaDemo.dataSerializationDemo.presenter.ExternalizableDemoPresenter;

import java.util.Objects;

public class ExternalizableDemoActivity extends BaseActivity<ExternalizableDemoPresenter, ActivityExternalizableBinding> {
    String externalFilesDirString;
    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        externalFilesDirString = Objects.requireNonNull(this.getExternalFilesDir(null)).getAbsolutePath();
        initializeActivity();
        Log.i("SerializableDemoActivity  getAbsolutePath", externalFilesDirString);
    }

    private void initializeActivity() {
        getViewBound().btSave.setOnClickListener(v -> mPresenter.saveObject(new Teacher("li", 18), externalFilesDirString));
        getViewBound().btRead.setOnClickListener(v -> {
            Teacher teacher = mPresenter.readObject(externalFilesDirString);
            Log.i("SerializableDemoActivity", "name is " + teacher.getName() + "; age is " + teacher.getAge());
        });
    }

    @Override
    protected ExternalizableDemoPresenter onLoadPresenter() {
        return new ExternalizableDemoPresenter();
    }

    @Override
    protected ActivityExternalizableBinding getViewBinding() {
        return ActivityExternalizableBinding.inflate(getLayoutInflater());
    }
}