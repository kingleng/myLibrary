package com.example.leng.myapplication.base.hook.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by leng on 2020/3/9.
 */
public class InstrumentationProxy extends Instrumentation {

    Instrumentation mInstrumentation;

    public InstrumentationProxy(Instrumentation mInstrumentation) {
        this.mInstrumentation = mInstrumentation;
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                            Intent intent, int requestCode, Bundle options){
        Log.e("hook","hook 成功"+" -- who:"+who);
        try{

            Method execStartActivity = Instrumentation.class.getDeclaredMethod("execStartActivity",Context.class,IBinder.class,IBinder.class,Activity.class,Intent.class,int.class,Bundle.class);
            return (ActivityResult) execStartActivity.invoke(mInstrumentation,who,contextThread, token, target, intent, requestCode, options);
        }catch (Exception e){
            throw new RuntimeException(e);
        }


    }

}
