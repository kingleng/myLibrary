package com.example.leng.myapplication.base.plugIn;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * Created by leng on 2020/3/9.
 */
public class HCallback implements Handler.Callback {

    public static final int LAUNCH_ACTIVITY = 100;
    Handler mHandler;

    public HCallback(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(msg.what == LAUNCH_ACTIVITY){
            Object r = msg.obj;
            try {
                Intent intent = (Intent)FieldUtil.getField(r.getClass(),r,"intent");
                Intent target = intent.getParcelableExtra(Hookhelper.TARGET_INTENT);
                intent.setComponent(target.getComponent());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        mHandler.handleMessage(msg);
        return true;
    }


}
