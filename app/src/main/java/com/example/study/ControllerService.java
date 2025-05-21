package com.example.study;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.IBinder;

import com.example.studysdk.util.DebugUtil;
import com.liang.log.MLog;

public class ControllerService extends Service {
    public ControllerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleCommand(Intent intent) {
        DebugUtil.printStackTrace("ControllerService", Integer.MAX_VALUE);
        if (intent == null) {
            return;
        }
        final String action = intent.getAction();
        if (intent.getExtras() == null) {
            return;
        }
        final int status = intent.getExtras().getInt(PackageInstaller.EXTRA_STATUS);
        MLog.i("ControllerService, system install or uninstall result is " + status);
        //利用PackageInstaller的ServiceSender,接收apk安装、更新、卸载成功与否的消息
        boolean result = false;
        switch (status) {
            case PackageInstaller.STATUS_PENDING_USER_ACTION:
                break;
            case PackageInstaller.STATUS_SUCCESS:
                result = true;
                break;
            case PackageInstaller.STATUS_FAILURE:
            case PackageInstaller.STATUS_FAILURE_ABORTED:
            case PackageInstaller.STATUS_FAILURE_BLOCKED:
            case PackageInstaller.STATUS_FAILURE_CONFLICT:
            case PackageInstaller.STATUS_FAILURE_INCOMPATIBLE:
            case PackageInstaller.STATUS_FAILURE_INVALID:
            case PackageInstaller.STATUS_FAILURE_STORAGE:
                result = false;
                break;
            default:
                break;
        }
    }


}