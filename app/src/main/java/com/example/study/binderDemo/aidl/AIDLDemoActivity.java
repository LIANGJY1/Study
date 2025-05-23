package com.example.study.binderDemo.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.study.base.BaseActivity;
import com.example.study.base.BasePresenter;
import com.example.study.databinding.ActivityAidldemoBinding;
import com.example.studysdk.Book;
import com.example.studysdk.BookManagerImpl;
import com.example.studysdk.IBookManager;

import java.util.List;

public class AIDLDemoActivity extends BaseActivity<BasePresenter, ActivityAidldemoBinding> {

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initializeActivity();
    }

    private void initializeActivity() {
        getViewBound().btBindService.setOnClickListener(v -> bindService(new Intent(AIDLDemoActivity.this, BookService.class), serviceConnection, Context.BIND_AUTO_CREATE));
        getViewBound().btUnbindService.setOnClickListener(v -> unbindService(serviceConnection));
    }

//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            IUserManager iUserManager = IUserManager.Stub.asInterface(service);
//            User user = new User("wang", 20);
//            try {
//                iUserManager.addUser(user);
//                iUserManager.addUser(user);
//                List<User> userList = iUserManager.getUser();
//                for (int i = 0; i < userList.size(); i++) {
//                    User user1 = userList.get(i);
//                    Log.d("AIDLActivity", "user: " + user1.getName()+" "+ user1.getAge());
//                }
//            } catch (RemoteException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager iBookManager = BookManagerImpl.asInterface(service);
            Book book = new Book("wang", 20);
            try {
                iBookManager.addBook(book);
                iBookManager.addBook(book);
                List<Book> bookList = iBookManager.getBookList();
                for (int i = 0; i < bookList.size(); i++) {
                    Book book1 = bookList.get(i);
                    Log.d("AIDLActivity", "book: " + book1.getName()+" "+ book1.getAge());
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected BasePresenter onLoadPresenter() {
        return new BasePresenter();
    }

    @Override
    protected ActivityAidldemoBinding getViewBinding() {
        return ActivityAidldemoBinding.inflate(getLayoutInflater());
    }
}