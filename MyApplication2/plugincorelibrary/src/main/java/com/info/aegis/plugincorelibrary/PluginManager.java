package com.info.aegis.plugincorelibrary;

import android.app.ActivityThread;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.util.Log;

import com.info.aegis.plugincorelibrary.hook.InstrumentationProxy;
import com.info.aegis.plugincorelibrary.plugin.PlugInfo;
import com.info.aegis.plugincorelibrary.utils.Reflector;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by leng on 2020/7/15.
 */
public class PluginManager {

    public static final String TAG = "PluginManager";

    private Context context;

    /**
     * 插件包名 -- 插件信息 的映射
     */
    private final Map<String, PlugInfo> pluginPkgToInfoMap = new ConcurrentHashMap<>();

    private static PluginManager mInstance;

    public static PluginManager getInstance() {
        if(mInstance == null){
            throw new IllegalStateException("Please init the PluginManager first!");
        }
        return mInstance;
    }

    public static void init(Context context) throws Exception {
        mInstance = new PluginManager(context);
    }

    public PluginManager(Context context) {
        this.context = context;

        hookInstrumentationAndHandler();
    }

    private InstrumentationProxy proxy;

    protected void hookInstrumentationAndHandler() {
        try {
            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Instrumentation mInstrumentation = activityThread.getInstrumentation();
            proxy = new InstrumentationProxy(mInstrumentation,context.getPackageManager());
            Reflector.with(activityThread).field("mInstrumentation").set(proxy);

//            Handler mainHandler = (Handler) FieldUtil.getField(activityThread.getClass(),activityThread,"mH");
            Handler mainHandler = Reflector.with(activityThread).field("mH").get();
//            FieldUtil.setField(Handler.class,mainHandler,"mCallback",proxy.addHandler(mainHandler));
            Reflector.with(mainHandler).field("mCallback").set(proxy.addHandler(mainHandler));

        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    /** 加载插件模块 **/

    public PlugInfo loadPlugin(File pluginSrcDirFile) throws Exception {
        if (pluginSrcDirFile == null || !pluginSrcDirFile.exists()) {
            Log.e(TAG, "invalidate plugin file or Directory :" + pluginSrcDirFile);
            return null;
        }
        PlugInfo one = null;
        if (pluginSrcDirFile.isFile()) {
            one = createPlugInfo(pluginSrcDirFile);
            if (one != null) {
                savePluginToMap(one);
            }

        }
        return one;

    }

    public PlugInfo createPlugInfo(File pluginApk) throws Exception {
        return new PlugInfo(this,context,pluginApk);
    }

    private synchronized void savePluginToMap(PlugInfo plugInfo) {
        pluginPkgToInfoMap.put(plugInfo.getPackageName(), plugInfo);
    }


    /** 查询插件模块 **/
    public Map<String, PlugInfo> getPluginPkgToInfoMap() {
        return pluginPkgToInfoMap;
    }

    public PlugInfo getLoadedPlugin(Intent intent) {
        return getLoadedPlugin(intent.getComponent());
    }

    public PlugInfo getLoadedPlugin(ComponentName component) {
        if (component == null) {
            return null;
        }
        return this.getLoadedPlugin(component.getPackageName());
    }

    public PlugInfo getLoadedPlugin(String packageName) {
        return this.pluginPkgToInfoMap.get(packageName);
    }

    /** 获取activity service的代理类 **/
    public InstrumentationProxy  getInstrumentation() {
        return this.proxy;
    }

    public Context getHostContext() {
        return this.context;
    }

    /** 查询已加载插件模块列表，获取目标activity，servoice信息 **/
    public ResolveInfo resolveActivity(Intent intent) {
        return this.resolveActivity(intent, 0);
    }

    public ResolveInfo resolveActivity(Intent intent, int flags) {
        for (PlugInfo plugin : this.pluginPkgToInfoMap.values()) {
            ResolveInfo resolveInfo = plugin.resolveActivity(intent, flags);
            if (null != resolveInfo) {
                return resolveInfo;
            }
        }

        return null;
    }

    /**
     * used in PluginPackageManager, do not invoke it from outside.
     */
    @Deprecated
    public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
        List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();

        for (PlugInfo plugin : this.pluginPkgToInfoMap.values()) {
            List<ResolveInfo> result = plugin.queryIntentActivities(intent, flags);
            if (null != result && result.size() > 0) {
                resolveInfos.addAll(result);
            }
        }

        return resolveInfos;
    }
}
