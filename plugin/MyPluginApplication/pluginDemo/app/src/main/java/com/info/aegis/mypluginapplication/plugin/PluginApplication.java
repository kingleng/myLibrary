package com.info.aegis.mypluginapplication.plugin;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.info.aegis.baselibrary.TopApplication;

public class PluginApplication extends TopApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        Log.e("asd","PluginApplication  attachBaseContext");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("asd","PluginApplication  onCreate");
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=5db11808");

    }
}