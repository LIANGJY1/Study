package com.example.study.provider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;

import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityProviderBinding;

public class ProviderActivity extends BaseActivity<ProviderPresenter, ActivityProviderBinding> {

    @Override
    protected ProviderPresenter onLoadPresenter() {
        return new ProviderPresenter();
    }

    @Override
    protected ActivityProviderBinding getViewBinding() {
        return ActivityProviderBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().readContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ProviderActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProviderActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                }else{
                    mPresenter.readContacts(ProviderActivity.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mPresenter.readContacts(ProviderActivity.this);
                }else{
                    Toast.makeText(this, "YOu denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


}