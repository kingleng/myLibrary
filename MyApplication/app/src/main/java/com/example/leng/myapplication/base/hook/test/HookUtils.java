package com.example.leng.myapplication.base.hook.test;

import android.app.Activity;
import android.app.Instrumentation;

import java.lang.reflect.Field;

/**
 * Created by leng on 2020/3/9.
 */
public class HookUtils {

    public static void replaceActivityInstrumentation(Activity activity){
        try{
            //得到activity的mInstrumentation字段
            Field field = Activity.class.getDeclaredField("mInstrumentation");
            //取消java的权限控制检查
            field.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation)field.get(activity);
            Instrumentation instrumentationProxy = new InstrumentationProxy(instrumentation);
            field.set(activity,instrumentationProxy);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void replaceContextInstrumentation(){
        try{
            //得到ActivityThread类
            Class<?> activityThreadClaszz = Class.forName("android.app.ActivityThread");
            Field activityThreadField = activityThreadClaszz.getDeclaredField("sCurrentActivityThread");
            //取消java的权限控制检查
            activityThreadField.setAccessible(true);
            Object currentActivityThread = activityThreadField.get(null);
            Field mInstrumentationField = activityThreadClaszz.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation)mInstrumentationField.get(currentActivityThread);
            Instrumentation instrumentationProxy = new InstrumentationProxy(instrumentation);
            mInstrumentationField.set(currentActivityThread,instrumentationProxy);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
