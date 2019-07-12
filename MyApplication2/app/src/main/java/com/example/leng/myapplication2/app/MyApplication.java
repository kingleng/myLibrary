package com.example.leng.myapplication2.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.leng.myapplication2.router.Router;
import com.example.leng.myapplication2.ui.tools.MyLog;

import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import rxhttp.BuildConfig;
import rxhttp.HttpSender;
import rxhttp.wrapper.param.DeleteRequest;
import rxhttp.wrapper.param.GetRequest;
import rxhttp.wrapper.param.Param;
import rxhttp.wrapper.param.PostRequest;
import rxhttp.wrapper.param.PutRequest;

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

        httpSenderInit();
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

    private void httpSenderInit() {
        //HttpSender初始化，自定义OkHttpClient对象,非必须
//        HttpSender.init(new OkHttpClient());

        //设置RxJava 全局异常处理
        RxJavaPlugins.setErrorHandler(throwable -> {
            Log.e("LJX", "setErrorHandler=" + throwable);
            /**
             * RxJava2的一个重要的设计理念是：不吃掉任何一个异常,即抛出的异常无人处理，便会导致程序崩溃
             * 这就会导致一个问题，当RxJava2“downStream”取消订阅后，“upStream”仍有可能抛出异常，
             * 这时由于已经取消订阅，“downStream”无法处理异常，此时的异常无人处理，便会导致程序崩溃
             */
        });
        HttpSender.setDebug(BuildConfig.DEBUG);
        HttpSender.setOnParamAssembly(new Function<Param, Param>() {
            /**
             * <p>在这里可以为所有请求添加公共参数，也可以为url统一添加前缀或者后缀
             * <p>子线程执行，每次发送请求前都会被回调
             *
             * @param p Param
             * @return 修改后的Param对象
             */
            @Override
            public Param apply(Param p) {

                //根据不同请求添加不同参数
                if (p instanceof GetRequest) {

                } else if (p instanceof PostRequest) {

                } else if (p instanceof PutRequest) {

                } else if (p instanceof DeleteRequest) {

                }
                //为url 添加前缀或者后缀  并重新设置url
                //p.setUrl("");
                return p.add("versionName", "1.0.0")//添加公共参数
                        .addHeader("deviceType", "android"); //添加公共请求头
            }
        });
    }
}
