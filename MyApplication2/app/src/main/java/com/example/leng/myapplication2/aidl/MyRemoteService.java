package com.example.leng.myapplication2.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MyRemoteService extends Service {
    public MyRemoteService() {
    }


    MyBinder myBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(myBinder == null){
            myBinder = new MyBinder();
        }
        return myBinder;
    }
}
