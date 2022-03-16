package com.info.aegis.baselibrary;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.info.aegis.baselibrary.GLog.GLog;
import com.info.aegis.baselibrary.utils.AppUtils;
import com.info.aegis.baselibrary.utils.CustomToast;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//import io.reactivex.functions.Function;
//import io.reactivex.plugins.RxJavaPlugins;
//import rxhttp.wrapper.param.Method;
//import rxhttp.wrapper.param.Param;
//import rxhttp.wrapper.param.RxHttp;

/**
 * 作者：jksfood on 2018/3/30 11:13
 */

public class TopApplication extends Application {

    public static TopApplication mTopApplication;

    //常量
    public static final String APP_SP = "APP_SP";

    public static final int Key_Board_Search = 888;//手写键盘输入查询方式

    //系统参数
    public static String mAndroidVersion;
    public static String memory;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTopApplication = this;

        handleSSLHandshake();

        //工具类 (勿动)
        AppUtils.syncIsDebug(this); //检查测试/正式包 必须放在第一个
//        Utils.init(this); //通用工具集合
        CustomToast.init(this); //Toast工具
//        TextFilter.init(this); //敏感词过滤
        GLog.init(); //Log打印
//        BlockUtil.init(this); //阻塞监测工具
//        LogReportUtil.init(this); //异常日志工具

//        httpSenderInit();

    }

//    private void httpSenderInit() {
//
//        //设置RxJava 全局异常处理
//        RxJavaPlugins.setErrorHandler(throwable -> {
//            Log.e("LJX", "setErrorHandler=" + throwable);
//            /**
//             * RxJava2的一个重要的设计理念是：不吃掉任何一个异常,即抛出的异常无人处理，便会导致程序崩溃
//             * 这就会导致一个问题，当RxJava2“downStream”取消订阅后，“upStream”仍有可能抛出异常，
//             * 这时由于已经取消订阅，“downStream”无法处理异常，此时的异常无人处理，便会导致程序崩溃
//             */
//        });
//        RxHttp.setDebug(BuildConfig.DEBUG);
//        RxHttp.setOnParamAssembly(new Function<Param, Param>() {
//            @Override
//            public Param apply(Param p) {
//                Method method = p.getMethod();
//                //根据不同请求添加不同参数
//                if (method.isGet()) { //Get请求
//
//                } else if (method.isPost()) { //Post请求
//
//                }
//                //可以通过 p.getSimpleUrl() 拿到url更改后，重新设置
//                //p.setUrl("");
//                return p.add("versionName", "1.0.0")//添加公共参数
//                        .addHeader("deviceType", "android"); //添加公共请求头
//            }
//        });
//    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Application getTopApplication() {
        return mTopApplication;
    }


    //忽略https的证书校验
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }


}
