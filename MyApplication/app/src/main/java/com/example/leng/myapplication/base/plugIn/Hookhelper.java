package com.example.leng.myapplication.base.plugIn;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by leng on 2020/3/9.
 */
public class Hookhelper {
    public static final String TARGET_INTENT = "target_intent";
    public static final String TARGET_INTENT_NAME = "target_intent_name";

    public static void hookAMS() throws Exception{
        Object defaultSingleton = null;
        if(Build.VERSION.SDK_INT >= 26){
            Class<?> activityManagerClazz = Class.forName("android.app.ActivityManager");
            defaultSingleton = FieldUtil.getField(activityManagerClazz,null,"IActivityManagerSingleton");
        }else{
            Class<?> activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative");
            defaultSingleton = FieldUtil.getField(activityManagerNativeClazz,null,"gDefault");
        }

        Class<?> singletonClazz = Class.forName("android.util.Singleton");
        Field mInstanceField = FieldUtil.getField(singletonClazz,"mInstance");
        Object iActivityManager = mInstanceField.get(defaultSingleton);
        Class<?> iActivityManagerClazz = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{iActivityManagerClazz},new IActivityManagerProxy(iActivityManager));
        mInstanceField.set(defaultSingleton,proxy);

    }

    public static void hookHandler() throws Exception{
        Class<?> activityManagerClazz = Class.forName("android.app.ActivityThread");
        Object currentActivityThread = FieldUtil.getField(activityManagerClazz,null,"sCurrentActivityThread");


        Field mHField = FieldUtil.getField(activityManagerClazz,"mH");
        Handler mH = (Handler)mHField.get(currentActivityThread);
        FieldUtil.setField(Handler.class,mH,"mCallback",new HCallback(mH));

    }

    public static void hookInstrumentation(Context context) throws Exception{
        Class<?> contextImplClass = Class.forName("android.app.ContextImpl");
        Field mMainThreadField  =FieldUtil.getField(contextImplClass,"mMainThread");
        Object activityThread = mMainThreadField.get(context);

        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field mInstrumentationField = FieldUtil.getField(activityThreadClass,"mInstrumentation");
        InstrumentationProxy proxy = new InstrumentationProxy((Instrumentation)mInstrumentationField.get(activityThread),context.getPackageManager());
        FieldUtil.setField(activityThreadClass,activityThread,"mInstrumentation",proxy);
    }

}
