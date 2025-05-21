package com.example.study.pms;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.study.ControllerService;
import com.example.study.base.BasePresenter;
import com.example.studysdk.util.ApkUtil;
import com.liang.log.MLog;

public class PMSPresenter extends BasePresenter {
    @SuppressLint("SdCardPath")
    public void install(Context context) {
        MLog.i("copyApkFromAssetsToDevice start");
        String assetFileName = "study.apk";
        ApkUtil.copyApkFromAssetsToDevice(context.getApplicationContext(), assetFileName, "/data/user/0/com.example.study/files/Download/" + assetFileName);
        ApkUtil.install("/data/user/0/com.example.study/files/Download/"  + assetFileName, context.getApplicationContext(), "ACTION_INSTALL_SILENTLY", ControllerService.class);
        MLog.i("copyApkFromAssetsToDevice end");
    }
}
