package com.cpsdna.app.mykotlinapplication;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.cpsdna.app.mykotlinapplication.util.AppUtils;
import com.cpsdna.app.mykotlinapplication.util.BlockUtil;
import com.cpsdna.app.mykotlinapplication.util.CustomToast;
import com.cpsdna.app.mykotlinapplication.util.GLog.GLog;
import com.cpsdna.app.mykotlinapplication.util.LogReportUtil;
import com.cpsdna.app.mykotlinapplication.util.Network.NetWorkStateUtil;
import com.cpsdna.app.mykotlinapplication.util.TextFilter;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import rxhttp.HttpSender;
import rxhttp.wrapper.param.DeleteRequest;
import rxhttp.wrapper.param.GetRequest;
import rxhttp.wrapper.param.Param;
import rxhttp.wrapper.param.PostRequest;
import rxhttp.wrapper.param.PutRequest;

/**
 * 作者：jksfood on 2018/3/30 11:13
 */

public class TopApplication extends Application {

    public static TopApplication mTopApplication;

    //常量
    public static final String APP_SP = "APP_SP";

    public static final int Key_Board_Search = 888;//手写键盘输入查询方式

    public static Typeface tf;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTopApplication = this;

        tf = Typeface.createFromAsset(getAssets(), "SourceHanSerifCN-Bold.otf.ttf");

        handleSSLHandshake();


        SharePreHelper.setAsrType("cloud");//设置语音识别类型为  在线语音识别
        //工具类 (勿动)
        AppUtils.syncIsDebug(this); //检查测试/正式包 必须放在第一个
        Utils.init(this); //通用工具集合
        CustomToast.init(this); //Toast工具
        TextFilter.init(this); //敏感词过滤
        GLog.init(); //Log打印
        BlockUtil.init(this); //阻塞监测工具
        LogReportUtil.init(this); //异常日志工具

        NetWorkStateUtil.init(); //网络监控工具

        httpSenderInit();

        InitializationConfig config = InitializationConfig.newBuilder(this)
                // 全局连接服务器超时时间，单位毫秒，默认10s。
                .connectionTimeout(16 * 1000)
                // 全局等待服务器响应超时时间，单位毫秒，默认10s。
                .readTimeout(16 * 1000)
                // 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
                .cacheStore(
                        // 如果不使用缓存，setEnable(false)禁用。
                        new DBCacheStore(this).setEnable(true)
                )
                // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现CookieStore接口。
                .cookieStore(
                        // 如果不维护cookie，setEnable(false)禁用。
                        new DBCookieStore(this).setEnable(true)
                )
                // 配置网络层，默认URLConnectionNetworkExecutor，如果想用OkHttp：OkHttpNetworkExecutor。
//                .networkExecutor(new OkHttpNetworkExecutor())
//                // 全局通用Header，add是添加，多次调用add不会覆盖上次add。
//                .addHeader()
//                // 全局通用Param，add是添加，多次调用add不会覆盖上次add。
//                .addParam()
//                .sslSocketFactory() // 全局SSLSocketFactory。
//                .hostnameVerifier() // 全局HostnameVerifier。
//                .retry(x) // 全局重试次数，配置后每个请求失败都会重试x次。
                .build();
        NoHttp.initialize(config);
        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("NoHttpSample");// 打印Log的tag。

//        //获得设备ID
//        String deviceId = UtilMethod.generateUniqueDeviceId();
//        if (AppUtils.isDebug()){
//                SharePreHelper.setDeviceId(deviceId);
////            SharePreHelper.setDeviceId("7f4f68ffd8e56ec63130aa0e09c2f681");
//        }else {
//            SharePreHelper.setDeviceId(deviceId);
//        }

//        //接口加密Key
//        String g1 = JniUtils.getG1();
//        AES.key = g1;
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
