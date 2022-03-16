package com.info.aegis.mypluginapplication.plugin.base;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.info.aegis.baselibrary.base.LibraryActivity;
import com.info.aegis.baselibrary.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：jksfood on 2017/9/1 14:54
 */

public class BaseActivity extends LibraryActivity {
    public static final String TAG = "BaseActivity";
    protected static final List<BaseActivity> activitys = new ArrayList<>();

    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        synchronized (activitys) {
            //打开Activity的时候添加集合
            activitys.add(this);
        }

        fullScreen();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        fullScreen();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean isNetWorkAvailable(Context context){
        NetworkUtils.isOnline();
        return NetworkUtils.isNetWorkAvailable(context);
    }

    /**
     * 全屏
     */
    public void fullScreen() {
        //API19以上，隐藏底部虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        try {
            //隐藏导航栏
//            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            ActionBar actionBar = getActionBar();

            if (actionBar != null)
                actionBar.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        synchronized (activitys) {
            //Activity 页面关闭的时候,在集合中移除
            activitys.remove(this);
        }

    }

    /**
     * 退出程序时,关闭所有的activity
     */
    public void KillAll() {

        List<BaseActivity> copy;
        synchronized (activitys) {
            copy = new ArrayList<>(activitys);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
        //杀死进程
//        android.os.Process.killProcess(android.os.Process.myPid());

    }

    @Override
    public void requstInfo(String url, NetMsgListener listener){
        requstInfo(url,null, listener);
    }


}
