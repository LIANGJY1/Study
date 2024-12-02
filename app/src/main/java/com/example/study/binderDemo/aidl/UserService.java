package com.example.study.binderDemo.aidl;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.study.R;
import com.example.study.notification.NotificationActivity;
import com.liang.ipccompat.MIpcService;
import com.reachauto.rms.ipccompat.interfaces.RIPCInterface;
import com.reachauto.rms.ipccompat.service.RIPCService;

import java.util.ArrayList;
import java.util.List;

public class UserService extends Service {
    private NotificationManager notificationManager;
    private Notification notification;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private void createNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notification = new NotificationCompat.Builder(this, "normal")
//                .setContentTitle("This is content title")
//                .setContentText("真正的平静，不是避开车马喧嚣，而是在心中修篱种菊。尽管如流往事，每一天都涛声依旧，只要我们消除执念，便可寂静安然。愿每个人，在纷呈世相中不会迷失荒径，可以端坐磐石上，醉倒落花前。")
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.a))
////                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setPriority(Notification.PRIORITY_MAX)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("真正的平静，不是避开车马喧嚣，而是在心中修篱种菊。尽管如流往事，每一天都涛声依旧，只要我们消除执念，便可寂静安然。愿每个人，在纷呈世相中不会迷失荒径，可以端坐磐石上，醉倒落花前。"))
//                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.a)))
                .build();
    }

    private ArrayList<User> userArrayList = new ArrayList<>();

    public UserService() {
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
//        addInterface(this);
        userArrayList.add(new User("zhang", 18));
        userArrayList.add(new User("li", 18));
        createNotification();

//        startForeground(1, notification);
        Log.i("SerializableDemoActivity", "name is ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
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

}