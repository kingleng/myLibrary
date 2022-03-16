package com.example.leng.myapplication.base.plugIn;

import android.annotation.TargetApi;
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
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.example.leng.myapplication.base.plugIn.manager.PlugInfo;
import com.example.leng.myapplication.base.plugIn.manager.PluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.greenrobot.eventbus.EventBus.TAG;

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

//    @Override
//    public void callActivityOnCreate(Activity activity, Bundle icicle) {
//        injectActivity(activity);
//        mInstrumentation.callActivityOnCreate(activity, icicle);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
//        injectActivity(activity);
//        mInstrumentation.callActivityOnCreate(activity, icicle, persistentState);
//    }

    protected void injectActivity(Activity activity) {
        final Intent intent = activity.getIntent();

        List<ResolveInfo> infos = mPackageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);
        if(infos ==null || infos.size() == 0){
            Context base = activity.getBaseContext();
            try {
                PlugInfo plugin = PluginManager.getInstance().getLoadedPlugin(intent);

                FieldUtil.setField(ContextThemeWrapper.class,activity,"mResources",plugin.getResources());
//                FieldUtil.setField(ContextThemeWrapper.class,activity,"mBase",plugin.getResources());

//                Reflector.with(base).field("mResources").set(plugin.getResources());
//                Reflector reflector = Reflector.with(activity);
//                reflector.field("mBase").set(plugin.createPluginContext(activity.getBaseContext()));
//                reflector.field("mApplication").set(plugin.getApplication());

//                // set screenOrientation
//                ActivityInfo activityInfo = plugin.getActivityInfo(PluginUtil.getComponent(intent));
//                if (activityInfo.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
//                    activity.setRequestedOrientation(activityInfo.screenOrientation);
//                }
//
//                // for native activity
//                ComponentName component = PluginUtil.getComponent(intent);
//                Intent wrapperIntent = new Intent(intent);
//                wrapperIntent.setClassName(component.getPackageName(), component.getClassName());
//                activity.setIntent(wrapperIntent);

            } catch (Exception e) {
                Log.w("InstrumentationProxy", e);
            }
        }

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
                        intent.setClassName(plugInfo.getPackageName(),resolveInfo.activityInfo.name);

                        Activity activity = super.newActivity(plugInfo.getClassLoader(), resolveInfo.activityInfo.name, intent);
                        activity.setIntent(intent);

//                        try {
//                            FieldUtil.setField(ContextThemeWrapper.class,activity,"mResources",plugInfo.getResources());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        // for 4.1+
                        Reflector.QuietReflector.with(activity).field("mResources").set(plugInfo.getResources());

                        return activity;
                    }
                }
            }
//            intent.setClassName("com.example.leng.myapplication","com.example.leng.myapplication.view.activity.MyNewActivity");
//            Activity activity = super.newActivity(cl,"com.example.leng.myapplication.view.activity.MyNewActivity",intent);
//            return activity;
//            return super.newActivity(cl,intentName,intent);
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
//                Intent intent = (Intent)FieldUtil.getField(r.getClass(),r,"intent");
//                Intent target = intent.getParcelableExtra(Hookhelper.TARGET_INTENT);
//                intent.setComponent(target.getComponent());

                ActivityInfo activityInfo = (ActivityInfo)FieldUtil.getField(r.getClass(),r,"activityInfo");

                Set<String> keys = PluginManager.getInstance().getPluginPkgToInfoMap().keySet();
                Iterator iterator = keys.iterator();
                if(iterator.hasNext()){
                    PlugInfo plugInfo = PluginManager.getInstance().getPluginPkgToInfoMap().get(iterator.next());
                    int theme = getTheme(plugInfo);
                    if (theme != 0) {
                        activityInfo.theme = theme;
                    }
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
