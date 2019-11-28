package com.cpsdna.app.mykotlinapplication.util.Network;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
 
    /** 网络不可用 */
    public static final int NO_NET_WORK = 0;
    /** 是wifi连接 */
    public static final int WIFI = 1;
    /** 不是wifi连接 */
    public static final int NO_WIFI = 2;
 
    private NetworkUtils(){
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
 
    /**
     * 判断是否打开网络
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context){
        boolean isAvailable = false ;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isAvailable()){
            isAvailable = true;
        }
        return isAvailable;
    }
 
    /**
     * 获取网络类型
     * @param context
     * @return
     */
    public static int getNetWorkType(Context context) {
        if (!isNetWorkAvailable(context)) {
            return NetworkUtils.NO_NET_WORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
            return NetworkUtils.WIFI;
        else
            return NetworkUtils.NO_WIFI;
    }
 
    /**
     * 判断当前网络是否为wifi
     * @param context
     * @return  如果为wifi返回true；否则返回false
     */
    @SuppressWarnings("static-access")
    public static boolean isWiFiConnected(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo.getType() == manager.TYPE_WIFI ? true : false;
    }
 
    /**
     * 判断MOBILE网络是否可用
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isMobileDataEnable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;
        isMobileDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return isMobileDataEnable;
    }
 
    /**
     * 判断wifi 是否可用
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isWifiDataEnable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }
 
    /**
     * 跳转到网络设置页面
     * @param activity
     */
    public static void GoSetting(Activity activity){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        activity.startActivity(intent);
    }
 
    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cn);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 在子线程里开启该方法，可检测当前网络是否能打开网页
     * true是可以上网，false是不能上网
     */
    public static boolean connectWeb(){

        URL url;
        try {
            url = new URL("https://www.baidu.com");
            InputStream stream = url.openStream();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void isOnline(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                if(connectWeb()){
                    EventBus.getDefault().post(new OnlineEvent(true));
                }

            }
        }.start();

    }

    static MyThread mMyThread;
    static class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            if(connectWeb()){
                EventBus.getDefault().post(new OnlineEvent(true));
            }
//            try {
//                boolean isOnline = false;
//                while (!isOnline){
//                    if(connectWeb()){
//                        EventBus.getDefault().post(new OnlineEvent(true));
//                        isOnline = true;
//                    }
//                    sleep(3000);
//                }
//            } catch (InterruptedException e) {
//
//            }
        }
    }
 
}