package com.example.study.binderDemo.aidl;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.studysdk.Book;
import com.example.studysdk.BookManagerImpl;

import java.util.ArrayList;
import java.util.List;

public class BookService extends Service {
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

    private ArrayList<Book> bookArrayList = new ArrayList<>();

    public BookService() {
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
//        addInterface(this);
        bookArrayList.add(new Book("zhang", 18));
        bookArrayList.add(new Book("li", 18));
        createNotification();

//        startForeground(1, notification);
        Log.i("SerializableDemoActivity", "name is ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private Binder binder = new BookManagerImpl() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookArrayList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookArrayList.add(book);
        }
    };

}