package com.info.aegis.plugincorelibrary.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.info.aegis.plugincorelibrary.PluginManager;
import com.info.aegis.plugincorelibrary.plugIn.PlugInfo;
import com.info.aegis.plugincorelibrary.utils.FieldUtil;
import com.info.aegis.plugincorelibrary.utils.Reflector;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by leng on 2020/3/9.
 */
public class InstrumentationProxy extends Instrumentation implements Handler.Callback{
    private Instrumentation mInstrumentation;
    private PackageManager mPackageManager;

    public InstrumentationProxy(Instrumentation mInstrumentation, PackageManager mPackageManager) {
        this.mInstrumentation = mInstrumentation;
        this.mPackageManager = mPackageManager;
    }

    Handler mHandler;
    public InstrumentationProxy addHandler(Handler mHandler){
        this.mHandler = mHandler;
        return this;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {


        List<ResolveInfo> infos = mPackageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);
        if(infos ==null || infos.size() == 0){
            checkIntent(intent);
            intent.putExtra(Hookhelper.TARGET_INTENT_NAME,intent.getComponent().getClassName());
            intent.putExtra(Hookhelper.TARGET_INTENT_PACKAGE,intent.getComponent().getPackageName());
            intent.setClassName(who,"com.info.aegis.com.info.aegis.plugincorelibrary.StubActivity");
        }
        try{
            Method execMethod = Instrumentation.class.getDeclaredMethod("execStartActivity",Context.class,IBinder.class,IBinder.class,Activity.class,Intent.class,int.class,Bundle.class);
            return (ActivityResult)execMethod.invoke(mInstrumentation,who,contextThread,token,target,intent,requestCode,options);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void checkIntent(Intent intent){
        ComponentName componentName = intent.getComponent();
        String className = componentName.getClassName();

        Map<String, PlugInfo> plugInfoMap = PluginManager.getInstance().getPluginPkgToInfoMap();
        if(plugInfoMap !=null){
            Iterator<PlugInfo> plugInfos  = plugInfoMap.values().iterator();
            while (plugInfos.hasNext()){
                PlugInfo plugInfo = plugInfos.next();
                if(plugInfo !=null){
                    Iterator<ResolveInfo> activitys = plugInfo.getActivities().iterator();
                    while (activitys.hasNext()){
                        ResolveInfo resolveInfo = activitys.next();
                        if(TextUtils.equals(resolveInfo.activityInfo.name,className)){
                            intent.setClassName(resolveInfo.activityInfo.packageName,resolveInfo.activityInfo.name);
                        }
                    }
                }
            }
        }
    }

    public Activity newActivity(ClassLoader cl, String className, Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String intentName = intent.getStringExtra(Hookhelper.TARGET_INTENT_NAME);
        String intentPackage = intent.getStringExtra(Hookhelper.TARGET_INTENT_PACKAGE);
        if(!TextUtils.isEmpty(intentName)){

            PlugInfo plugInfo = PluginManager.getInstance().getLoadedPlugin(intentPackage);

            intent.setClassName(plugInfo.getPackageName(),intentName);
            Activity activity = super.newActivity(plugInfo.getClassLoader(), intentName, intent);
            activity.setIntent(intent);

            Reflector.QuietReflector.with(activity).field("mResources").set(plugInfo.getResources());
            return activity;
        }
        Activity activity = super.newActivity(cl,className,intent);
        return activity;
    }

    public static final int LAUNCH_ACTIVITY = 100;

    @Override
    public boolean handleMessage(Message msg) {
        if(msg.what == LAUNCH_ACTIVITY){
            // ActivityClientRecord r
            Object r = msg.obj;
            try {

                ActivityInfo activityInfo = (ActivityInfo) FieldUtil.getField(r.getClass(),r,"activityInfo");
                Intent intent = (Intent) FieldUtil.getField(r.getClass(),r,"intent");
                String packageName = intent.getStringExtra(Hookhelper.TARGET_INTENT_PACKAGE);

                PlugInfo plugInfo = PluginManager.getInstance().getLoadedPlugin(packageName);
                int theme = getTheme(plugInfo);
                if (theme != 0) {
                    activityInfo.theme = theme;
                }

            }catch (Exception e){
                e.printStackTrace();
            }

//            mHandler.handleMessage(msg);
        }
        return false;
    }


    public static int getTheme(PlugInfo plugInfo) {

        if (null == plugInfo) {
            return 0;
        }

        ApplicationInfo appInfo = plugInfo.getApplication().getApplicationInfo();
        if (null != appInfo && appInfo.theme != 0) {
            return appInfo.theme;
        }

        return selectDefaultTheme(0, Build.VERSION.SDK_INT);
    }

    public static int selectDefaultTheme(final int curTheme, final int targetSdkVersion) {
        return selectSystemTheme(curTheme, targetSdkVersion,
                android.R.style.Theme,
                android.R.style.Theme_Holo,
                android.R.style.Theme_DeviceDefault,
                android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
    }

    public static int selectSystemTheme(final int curTheme, final int targetSdkVersion, final int orig, final int holo, final int dark, final int deviceDefault) {
        if (curTheme != 0) {
            return curTheme;
        }

        if (targetSdkVersion < 11 /* Build.VERSION_CODES.HONEYCOMB */) {
            return orig;
        }

        if (targetSdkVersion < 14 /* Build.VERSION_CODES.ICE_CREAM_SANDWICH */) {
            return holo;
        }

        if (targetSdkVersion < 24 /* Build.VERSION_CODES.N */) {
            return dark;
        }

        return deviceDefault;
    }



}
