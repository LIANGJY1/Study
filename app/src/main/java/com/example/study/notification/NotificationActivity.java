package com.example.study.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityNotificationBinding;
import com.example.study.homepage.activity.MainActivity;

public class NotificationActivity extends BaseActivity<BasePresenter, ActivityNotificationBinding> {

    private NotificationManager notificationManager;
    private Notification notification;

    @Override
    protected void afterSetContentView() {
        initializeView();
    }

    private void initializeView() {
        getViewBound().btCreateNotification.setOnClickListener(v -> createNotification());
        getViewBound().btNotifyNotification.setOnClickListener(v -> notificationManager.notify(1, notification));
        getViewBound().btCancelNotification.setOnClickListener(v -> notificationManager.cancel(1));
    }

    private void createNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notification = new NotificationCompat.Builder(NotificationActivity.this, "normal")
                .setContentTitle("This is content title")
                .setContentText("真正的平静，不是避开车马喧嚣，而是在心中修篱种菊。尽管如流往事，每一天都涛声依旧，只要我们消除执念，便可寂静安然。愿每个人，在纷呈世相中不会迷失荒径，可以端坐磐石上，醉倒落花前。")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.a))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("真正的平静，不是避开车马喧嚣，而是在心中修篱种菊。尽管如流往事，每一天都涛声依旧，只要我们消除执念，便可寂静安然。愿每个人，在纷呈世相中不会迷失荒径，可以端坐磐石上，醉倒落花前。"))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.a)))
                .build();
    }

    @Override
    protected BasePresenter onLoadPresenter() {
        return null;
    }

    @Override
    protected ActivityNotificationBinding getViewBinding() {
        return ActivityNotificationBinding.inflate(getLayoutInflater());
    }
}