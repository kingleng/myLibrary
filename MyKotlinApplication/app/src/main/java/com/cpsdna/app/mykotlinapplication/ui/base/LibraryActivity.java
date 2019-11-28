package com.cpsdna.app.mykotlinapplication.ui.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.cpsdna.app.mykotlinapplication.util.Network.NetworkUtils;
import com.cpsdna.app.mykotlinapplication.util.Network.OnlineEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by leng on 2019/9/9.
 */
public class LibraryActivity extends AppCompatActivity {

    public boolean isOnline = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fullScreen();
    }

    public boolean isNetWorkAvailable(Context context){
        NetworkUtils.isOnline();
        return NetworkUtils.isNetWorkAvailable(context);
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    //数据更新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnlineEvent event) {
        isOnline = event.isOnline;
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
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();

            if (actionBar != null)
                actionBar.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
