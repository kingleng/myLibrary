package com.info.aegis.plugincore;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.util.Log;

import com.info.aegis.plugincore.hook.InstrumentationProxy;
import com.info.aegis.plugincore.plugIn.PlugInfo;
import com.info.aegis.plugincore.utils.FieldUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by leng on 2020/3/12.
 */
public class PluginManager {

    private static final String TAG = "PluginManager";

    private Context context;

    /**
     * 私有目录中存储插件的路径
     */
    private String privatePath;
    private String dexOutputPath;
    private File dexInternalStoragePath;

    /**
     * 自定义的Instrumentation代理，用来加载插件的activity
     */
    private InstrumentationProxy proxy;

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

    public static void init(Context context) throws Exception{
        mInstance = new PluginManager(context);
    }

    public PluginManager(Context context) {
        this.context = context;

        //插件apk存放地址
        privatePath =context.getFilesDir().getAbsolutePath()+"/plugin";
        File pFile = new File(privatePath);
        if(!pFile.exists()){
            pFile.mkdir();
        }
        //classloader解压插件apk后的dex文件存放地址
        dexOutputPath = context.getFilesDir().getAbsolutePath()+"/plugin_file";
        File dFile = new File(dexOutputPath);
        if(!dFile.exists()){
            dFile.mkdir();
        }
        //插件apk里的jar包，so库存放地址
        String dexInternalStorage = context.getFilesDir().getAbsolutePath()+"/plugin_opt";
        dexInternalStoragePath = new File(dexInternalStorage);
        if(!dexInternalStoragePath.exists()){
            dexInternalStoragePath.mkdir();
        }

        //替换自定义的instrumentation来加载插件里的activity
        hookInstrumentationAndHandler();
    }

    protected void hookInstrumentationAndHandler() {
        try {
            Class<?> contextImplClass = Class.forName("android.app.ContextImpl");
            Field mMainThreadField  = FieldUtil.getField(contextImplClass,"mMainThread");
            Object activityThread = mMainThreadField.get(context);

            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field mInstrumentationField = FieldUtil.getField(activityThreadClass,"mInstrumentation");
            proxy = new InstrumentationProxy((Instrumentation)mInstrumentationField.get(activityThread),context.getPackageManager());
            FieldUtil.setField(activityThreadClass,activityThread,"mInstrumentation",proxy);

            Handler mainHandler = (Handler) FieldUtil.getField(activityThread.getClass(),activityThread,"mH");
            FieldUtil.setField(Handler.class,mainHandler,"mCallback",proxy.addHandler(mainHandler));

        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

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

    public PlugInfo loadPlugin(File pluginSrcDirFile) throws Exception{
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

    private synchronized void savePluginToMap(PlugInfo plugInfo) {
        pluginPkgToInfoMap.put(plugInfo.getPackageName(), plugInfo);
    }

    public PlugInfo createPlugInfo(File pluginApk) throws Exception{
        return new PlugInfo(this,context,pluginApk);
    }

    public InstrumentationProxy  getInstrumentation() {
        return this.proxy;
    }

    public Context getHostContext() {
        return this.context;
    }

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
