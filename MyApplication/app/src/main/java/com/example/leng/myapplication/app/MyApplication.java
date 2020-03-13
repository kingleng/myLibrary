package com.example.leng.myapplication.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.leng.myapplication.base.plugIn.Hookhelper;
import com.example.leng.myapplication.base.plugIn.manager.PluginManager;
import com.example.leng.myapplication.view.tools.MyLog;

import java.io.File;

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
//            Hookhelper.hookAMS();
//            Hookhelper.hookHandler();

            Hookhelper.hookInstrumentation(base);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
