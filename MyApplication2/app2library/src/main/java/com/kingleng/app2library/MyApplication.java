package com.kingleng.app2library;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by leng on 2019/7/26.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
