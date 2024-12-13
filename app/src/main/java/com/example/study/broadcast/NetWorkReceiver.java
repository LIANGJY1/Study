package com.example.study.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetWorkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        for (int i = 0; i < 3; i++) {
//            Log.i("NetWorkReceiver", String.valueOf(i));
//        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable()){
            Toast.makeText(context,"network is available",Toast.LENGTH_SHORT).show();
            Log.i("NetWorkReceiver", "manifest register, network is available");

        }else{
            Toast.makeText(context,"network is unavailable", Toast.LENGTH_SHORT).show();
            Log.i("NetWorkReceiver", "manifest register, network is unavailable");
        }
    }
}
