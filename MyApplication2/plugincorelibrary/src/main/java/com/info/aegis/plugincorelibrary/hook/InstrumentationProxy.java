package com.info.aegis.plugincorelibrary.hook;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.info.aegis.plugincorelibrary.Constants;
import com.info.aegis.plugincorelibrary.PluginManager;
import com.info.aegis.plugincorelibrary.delegate.StubActivity;
import com.info.aegis.plugincorelibrary.plugin.PlugInfo;
import com.info.aegis.plugincorelibrary.plugin.PluginContext;
import com.info.aegis.plugincorelibrary.utils.PluginUtil;
import com.info.aegis.plugincorelibrary.utils.Reflector;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by leng on 2020/3/9.
 */
public class InstrumentationProxy extends Instrumentation implements Handler.Callback {
    private static final String TAG = "InstrumentationProxy";
    private Instrumentation mInstrumentation;
    private PackageManager mPackageManager;

    public InstrumentationProxy(Instrumentation mInstrumentation, PackageManager mPackageManager) {
        this.mInstrumentation = mInstrumentation;
        this.mPackageManager = mPackageManager;
    }

    Handler mHandler;

    public InstrumentationProxy addHandler(Handler mHandler) {
        this.mHandler = mHandler;
        return this;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {

        List<ResolveInfo> infos = mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        if (infos == null || infos.size() == 0) {
            checkIntent(intent);
            intent.putExtra(Constants.TARGET_INTENT_NAME, intent.getComponent().getClassName());
            intent.putExtra(Constants.TARGET_INTENT_PACKAGE, intent.getComponent().getPackageName());
            intent.putExtra(Constants.KEY_IS_PLUGIN, true);
            intent.setClassName(who, StubActivity.class.getName());
        }
        try {
            Method execMethod = Instrumentation.class.getDeclaredMethod("execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class);
            return (ActivityResult) execMethod.invoke(mInstrumentation, who, contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkIntent(Intent intent) {
        ComponentName componentName = intent.getComponent();
        String className = componentName.getClassName();

        Map<String, PlugInfo> plugInfoMap = PluginManager.getInstance().getPluginPkgToInfoMap();
        if (plugInfoMap != null) {
            Iterator<PlugInfo> plugInfos = plugInfoMap.values().iterator();
            while (plugInfos.hasNext()) {
                PlugInfo plugInfo = plugInfos.next();
                if (plugInfo != null) {
                    Iterator<ActivityInfo> activitys = plugInfo.getActivities().iterator();
                    while (activitys.hasNext()) {
                        ActivityInfo activityInfo = activitys.next();
                        if (TextUtils.equals(activityInfo.name, className)) {
                            intent.setClassName(activityInfo.packageName, activityInfo.name);
                        }
                    }
                }
            }
        }
    }

    public Activity newActivity(ClassLoader cl, String className, Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String intentName = intent.getStringExtra(Constants.TARGET_INTENT_NAME);
        String intentPackage = intent.getStringExtra(Constants.TARGET_INTENT_PACKAGE);
        if (!TextUtils.isEmpty(intentName)) {

            PlugInfo plugInfo = PluginManager.getInstance().getLoadedPlugin(intentPackage);

            intent.setClassName(plugInfo.getPackageName(), intentName);
            Activity activity = super.newActivity(plugInfo.getClassLoader(), intentName, intent);
            activity.setIntent(intent);

            Reflector.QuietReflector.with(activity).field("mResources").set(plugInfo.getResources());
            return activity;
        }
        Activity activity = super.newActivity(cl, className, intent);
        return activity;
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        injectActivity(activity);
        mInstrumentation.callActivityOnCreate(activity, icicle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        injectActivity(activity);
        mInstrumentation.callActivityOnCreate(activity, icicle, persistentState);
    }

    protected void injectActivity(Activity activity) {
        final Intent intent = activity.getIntent();
        String intentName = intent.getStringExtra(Constants.TARGET_INTENT_NAME);
        String intentPackage = intent.getStringExtra(Constants.TARGET_INTENT_PACKAGE);
        if (!TextUtils.isEmpty(intentName)) {
            Context base = activity.getBaseContext();
            try {
                PlugInfo plugin = PluginManager.getInstance().getLoadedPlugin(intent);
                Reflector.with(base).field("mResources").set(plugin.getResources());
                Reflector reflector = Reflector.with(activity);
//                reflector.field("mBase").set(plugin.createPluginContext(activity.getBaseContext()));
                reflector.field("mBase").set(new PluginContext(PluginManager.getInstance().getHostContext(), plugin));
                reflector.field("mApplication").set(plugin.getApplication());

                // set screenOrientation
                ActivityInfo activityInfo = plugin.findActivityByClassName(intentName);
                if (activityInfo.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                    activity.setRequestedOrientation(activityInfo.screenOrientation);
                }

                // for native activity
                ComponentName component = PluginUtil.getComponent(intent);
                Intent wrapperIntent = new Intent(intent);
                wrapperIntent.setClassName(component.getPackageName(), component.getClassName());
                activity.setIntent(wrapperIntent);

            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }


    public static final int LAUNCH_ACTIVITY = 100;

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == LAUNCH_ACTIVITY) {
            // ActivityClientRecord r
            Object r = msg.obj;
            try {

//                ActivityInfo activityInfo = (ActivityInfo) FieldUtil.getField(r.getClass(), r, "activityInfo");
//                Intent intent = (Intent) FieldUtil.getField(r.getClass(), r, "intent");

                ActivityInfo activityInfo = Reflector.on(r.getClass()).bind(r).field("activityInfo").get();
                Intent intent =  Reflector.on(r.getClass()).bind(r).field("intent").get();
//                intent.setExtrasClassLoader(PluginManager.getInstance().getHostContext().getClassLoader());
                if(PluginUtil.isIntentFromPlugin(intent)){
                    int theme = PluginUtil.getTheme(PluginManager.getInstance().getHostContext(),intent);
                    if (theme != 0) {
                        Log.i(TAG, "resolve theme, current theme:" + activityInfo.theme + "  after :0x" + Integer.toHexString(theme));
                        activityInfo.theme = theme;
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "handleMessage Exception :: " + e.toString());
            }
        }
        return false;
    }

}
