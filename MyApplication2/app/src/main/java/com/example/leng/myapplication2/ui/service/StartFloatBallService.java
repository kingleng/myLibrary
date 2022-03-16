package com.example.leng.myapplication2.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.leng.myapplication2.ui.service.floatball.ViewManager;

public class StartFloatBallService extends Service {

    public StartFloatBallService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        ViewManager manager = ViewManager.getInstance(this);
        manager.showFloatBall();
        super.onCreate();
    }

}