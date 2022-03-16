package com.info.aegis.plugincorelibrary.hook;

import android.content.Intent;
import android.text.TextUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by leng on 2020/3/9.
 */
public class IActivityManagerProxy implements InvocationHandler {

    private static final String TAG = "IActivityManagerProxy";
    private Object mActivityManager;

    public IActivityManagerProxy(Object mActivityManager) {
        this.mActivityManager = mActivityManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(TextUtils.equals("startActivity",method.getName())){
            Intent intent = null;
            int index = 0;
            for(int i=0;i<args.length;i++){
                if(args[i] instanceof Intent){
                    index = i;
                    break;
                }
            }

            intent = (Intent)args[index];
            Intent subIntent = new Intent();
            String packageName = "com.example.leng.myapplication";
            subIntent.setClassName(packageName,packageName+".view.activity.StubActivity");
            subIntent.putExtra(Hookhelper.TARGET_INTENT,intent);
            args[index] = subIntent;
        }
        return method.invoke(mActivityManager,args);
    }

}
