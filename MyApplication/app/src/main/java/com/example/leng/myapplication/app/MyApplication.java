package com.example.leng.myapplication.app;

import android.app.Application;

import com.example.leng.myapplication.view.tools.MyLog;

/**
 * Created by leng on 2017/3/2.
 */

public class MyApplication extends Application {

    /**
     * 当前运行环境
     */
    public static final Integer CONFIG = Constants.BUG;

    @Override
    public void onCreate() {
        super.onCreate();
        //如果是运营环境，则关闭日志打印..
        if (CONFIG == Constants.RELEASE) {
            MyLog.isDebug = false;
        }
    }
}
