package com.example.leng.myapplication2.app;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.leng.myapplication2.router.Router;
import com.example.leng.myapplication2.view.tools.MyLog;

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

        //路由初始化
        Router.init(getApplicationContext());
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
//        return new HttpProxyCacheServer(this);
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .build();
    }
}
