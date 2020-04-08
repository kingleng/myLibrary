package com.example.leng.myapplication.base.plugIn.manager;

import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;

import com.example.leng.myapplication.base.plugIn.FieldUtil;
import com.example.leng.myapplication.base.plugIn.InstrumentationProxy;
import com.example.leng.myapplication.base.plugIn.ResourcesManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by leng on 2020/3/12.
 */
public class PluginManager {

    private static final String TAG = "PluginManager";
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

    private Context context;
    private String privatePath;
    private String dexOutputPath;

    /**
     * 插件包名 -- 插件信息 的映射
     */
    private final Map<String, PlugInfo> pluginPkgToInfoMap = new ConcurrentHashMap<String, PlugInfo>();

    public PluginManager(Context context) {
        this.context = context;
//        File pFile = context.getDir("plugin",Context.MODE_PRIVATE);
//        privatePath =pFile.getAbsolutePath();

        privatePath =context.getFilesDir().getAbsolutePath()+"/plugin";
        File pFile = new File(privatePath);
        if(!pFile.exists()){
            pFile.mkdir();
        }
//        privatePath = context.getFilesDir().getAbsolutePath()+"/plugin";
        dexOutputPath = context.getFilesDir().getAbsolutePath()+"/plugin_file";
        File dFile = new File(dexOutputPath);
        if(!dFile.exists()){
            dFile.mkdir();
        }

//        File dexFile = context.getDir("plugin_file",Context.MODE_PRIVATE);
//        dexOutputPath =dexFile.getAbsolutePath();

        hookInstrumentationAndHandler();
    }


    protected void hookInstrumentationAndHandler() {
        try {
            Class<?> contextImplClass = Class.forName("android.app.ContextImpl");
            Field mMainThreadField  = FieldUtil.getField(contextImplClass,"mMainThread");
            Object activityThread = mMainThreadField.get(context);

            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field mInstrumentationField = FieldUtil.getField(activityThreadClass,"mInstrumentation");
            InstrumentationProxy proxy = new InstrumentationProxy((Instrumentation)mInstrumentationField.get(activityThread),context.getPackageManager());
            FieldUtil.setField(activityThreadClass,activityThread,"mInstrumentation",proxy);

            Handler mainHandler = (Handler)FieldUtil.getField(activityThread.getClass(),activityThread,"mH");
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
            one = buildPlugInfo(pluginSrcDirFile);
            if (one != null) {
                savePluginToMap(one);
            }

        }
        return one;

    }

    private synchronized void savePluginToMap(PlugInfo plugInfo) {
        pluginPkgToInfoMap.put(plugInfo.getPackageName(), plugInfo);
    }

    public PlugInfo buildPlugInfo(File pluginApk) throws Exception{

        PlugInfo info = new PlugInfo();
        info.setId(pluginApk.getName());

        //privateFile : /data/data/com.lange.plugin/app_plugins-opt/xxxx.apk
        //需要把sd卡上的apk保存到应用的私有路径
        //privateFile 是apk保存的私有路径
        File privateFile = new File(privatePath,pluginApk.getName());

        info.setFilePath(privateFile.getAbsolutePath());
        //Copy Plugin to Private Dir
        //如果是载入sd卡上的apk，先拷贝到应用私有路径下
        if (!privateFile.exists()) {
            copyApkToPrivatePath(pluginApk, privateFile);
        }

        //dexClassLoader可以载入 jar 、apk 文件
        String dexPath = privateFile.getAbsolutePath();
        //Load Plugin Manifest
        //获取一些apk里面 MainFest.xml的一些内容，包括 pacageName 、Activity 、application 等
        PluginManifestUtil.setManifestInfo(context, dexPath, info,null);

        Resources resources = createResources(context,dexPath,pluginApk);
        info.setResources(resources);


        //Load  classLoader for Plugin
        PluginClassLoader pluginClassLoader = new PluginClassLoader(info, dexPath, dexOutputPath
                , getPluginLibPath(info).getAbsolutePath(), this.context.getClassLoader());
        //, getPluginLibPath(info).getAbsolutePath(), ClassLoader.getSystemClassLoader().getParent());
        info.setClassLoader(pluginClassLoader);
        ApplicationInfo appInfo = info.getPackageInfo().applicationInfo;
        String appClassName = null;
        if (appInfo != null) {
            appClassName = appInfo.name;
        }
        Application app = makeApplication(pluginClassLoader, appClassName);
        attachBaseContext(info, app);
        info.setApplication(app);
        Log.i(TAG, "buildPlugInfo: " + info);
        return info;
    }

    protected Resources createResources(Context context, String dexPath, File apk) throws Exception {
        if (true) {
            return ResourcesManager.createResourcesSimple(context, apk.getAbsolutePath());
        } else {
//            Resources hostResources = context.getResources();
//            AssetManager assetManager = createAssetManager(context, apk);
//            return new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());

                    //Load Plugin Res
        //通过反射的方式获取 AssetManager 的 addAssetPath 方法，把apk中的资源加入到 Resources 中
            AssetManager am = AssetManager.class.newInstance();
            am.getClass().getMethod("addAssetPath", String.class).invoke(am, dexPath);
//            info.setAssetManager(am);
            Resources hotRes = context.getResources();
            Resources res = new Resources(am, hotRes.getDisplayMetrics(), hotRes.getConfiguration());
            return res;
        }
    }

    /**
     * 将Apk复制到私有目录
     *
     * @param pluginApk    插件apk原始路径
     * @param targetPutApk 要拷贝到的目标位置
     */
    private void copyApkToPrivatePath(File pluginApk, File targetPutApk) {
        if (targetPutApk.exists()) {
            return;
        }
        try {
            FileUtils.copyFile(pluginApk, targetPutApk);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 构造插件的Application
     *
     * @param pluginClassLoader 类加载器
     * @param appClassName      类名
     * @return
     */
    private Application makeApplication(PluginClassLoader pluginClassLoader, String appClassName) {
        if (appClassName != null) {
            try {
                return (Application) pluginClassLoader.loadClass(appClassName).newInstance();
            } catch (Throwable ignored) {
            }
        }

        return new Application();
    }

    private void attachBaseContext(PlugInfo info, Application app) {
        try {
            Field mBase = ContextWrapper.class.getDeclaredField("mBase");
            mBase.setAccessible(true);
            mBase.set(app, new PluginContext(context.getApplicationContext(), info));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public File getPluginLibPath(PlugInfo plugInfo) {
        return new File(privatePath, plugInfo.getId() + "-dir/lib/");
    }
}
