package com.example.leng.myapplication.base.plugIn;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import com.example.leng.myapplication.base.plugIn.manager.PlugInfo;
import com.example.leng.myapplication.base.plugIn.manager.PluginManager;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by leng on 2020/3/9.
 */
public class InstrumentationProxy extends Instrumentation {
    private Instrumentation mInstrumentation;
    private PackageManager mPackageManager;

    public InstrumentationProxy(Instrumentation mInstrumentation, PackageManager mPackageManager) {
        this.mInstrumentation = mInstrumentation;
        this.mPackageManager = mPackageManager;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        List<ResolveInfo> infos = mPackageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);
        if(infos ==null || infos.size() == 0){
            intent.putExtra(Hookhelper.TARGET_INTENT_NAME,intent.getComponent().getClassName());
            intent.setClassName(who,"com.example.leng.myapplication.view.activity.StubActivity");
        }
        try{
            Method execMethod = Instrumentation.class.getDeclaredMethod("execStartActivity",Context.class,IBinder.class,IBinder.class,Activity.class,Intent.class,int.class,Bundle.class);
            return (ActivityResult)execMethod.invoke(mInstrumentation,who,contextThread,token,target,intent,requestCode,options);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Activity newActivity(ClassLoader cl, String className, Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String intentName = intent.getStringExtra(Hookhelper.TARGET_INTENT_NAME);
        if(!TextUtils.isEmpty(intentName)){
            Set<String> keys = PluginManager.getInstance().getPluginPkgToInfoMap().keySet();
            Iterator iterator = keys.iterator();
            if(iterator.hasNext()){
                PlugInfo plugInfo = PluginManager.getInstance().getPluginPkgToInfoMap().get(iterator.next());
                Iterator acs = plugInfo.getActivities().iterator();
                if(acs.hasNext()){
                    for(;acs.hasNext();){
                        ResolveInfo resolveInfo = (ResolveInfo)acs.next();
//                        if(resolveInfo.activityInfo.name.contains("GuideActivity")){
//                            intent.setClassName(plugInfo.getPackageName(),resolveInfo.activityInfo.name);
//                            return super.newActivity(plugInfo.getClassLoader(),resolveInfo.activityInfo.name,intent);
//                        }
                        intent.setClassName(plugInfo.getPackageName(),resolveInfo.activityInfo.name);
                        return super.newActivity(plugInfo.getClassLoader(),resolveInfo.activityInfo.name,intent);
                    }
                }
            }
//            return super.newActivity(cl,intentName,intent);
        }
        return super.newActivity(cl,className,intent);
    }



}
