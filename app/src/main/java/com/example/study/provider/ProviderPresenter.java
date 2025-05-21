package com.example.study.provider;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

import com.example.study.base.BasePresenter;
import com.liang.log.MLog;

import java.util.ArrayList;
import java.util.List;

public class ProviderPresenter extends BasePresenter {
    ArrayAdapter<String> adapter;

    List<String> contactsList = new ArrayList<String>();

    public void readContacts(Context context) {
        Cursor cursor = null;
        try{//ContactsContract.CommonDataKinds.Phone类做好了封装，提供了一个CONTENT_URI常量。
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            if(cursor != null){
                while(cursor.moveToNext()){//对Cursor对象进行遍历
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName + "\n" +number);
                    MLog.i("contacts, displayName: " + displayName + "; number: " + number);
                }
//                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
    }

}
