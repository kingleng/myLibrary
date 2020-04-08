package com.info.aegis.plugincore.plugIn;

import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageParser;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.info.aegis.plugincore.PluginManager;
import com.info.aegis.plugincore.utils.DexUtil;
import com.info.aegis.plugincore.utils.PackageParserCompat;
import com.info.aegis.plugincore.utils.Reflector;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

import static com.info.aegis.plugincore.utils.DexUtil.NATIVE_DIR;

/**
 * Created by leng on 2020/3/12.
 * 插件信息类
 */
public class PlugInfo implements Serializable {

    private String id;
    private String filePath;
    private PackageInfo packageInfo;
    private Map<String, ResolveInfo> activities;
    private Map<ComponentName, ActivityInfo> mActivityInfos;
    private ResolveInfo mainActivity;

    private transient ClassLoader classLoader;
    private transient Application application;
    private transient AssetManager assetManager;
    private transient Resources resources;
    private final File mNativeLibDir;
    private final Context mContext;

    private PluginManager mPluginManager;
//    protected final PackageParser.Package mPackage;
//    protected Context mPluginContext;

    private static String mPhoneInfo = "";//手机的体系机制类型 armeabi

    public PlugInfo(PluginManager pluginManager, Context context, File pluginApk) throws Exception {
        this.mPluginManager = pluginManager;
        this.mContext = context;

        getPhoneCPU();

        setId(pluginApk.getName());

//        //privateFile 是apk保存的私有路径
//        File privateFile = new File(privatePath,pluginApk.getName());
//        this.setFilePath(privateFile.getAbsolutePath());
//        if (privateFile.exists()) {
//            privateFile.delete();
//        }
//        copyApkToPrivatePath(pluginApk, privateFile);

        //dexClassLoader可以载入 jar 、apk 文件
        String dexPath = pluginApk.getAbsolutePath();
        mNativeLibDir = getDir(context, NATIVE_DIR);

//        this.mPackage = PackageParserCompat.parsePackage(context, pluginApk, PackageParser.PARSE_MUST_BE_APK);
//        this.mPackage.applicationInfo.metaData = this.mPackage.mAppMetaData;

        //Load Plugin Manifest
        //获取一些apk里面 MainFest.xml的一些内容，包括 pacageName 、Activity 、application 等
        PluginManifestUtil.setManifestInfo(context, dexPath, this,mPhoneInfo);

        if (pluginManager.getLoadedPlugin(packageInfo.packageName) != null) {
            throw new RuntimeException("plugin has already been loaded : " + packageInfo.packageName);
        }

//        // Cache activities
//        Map<ComponentName, ActivityInfo> activityInfos = new HashMap<ComponentName, ActivityInfo>();
//        for (PackageParser.Activity activity : this.mPackage.activities) {
//            activity.info.metaData = activity.metaData;
//            activityInfos.put(activity.getComponentName(), activity.info);
//        }
//        this.mActivityInfos = Collections.unmodifiableMap(activityInfos);

        Resources resources = createResources(context,pluginApk);
        setResources(resources);

        ClassLoader mClassLoader = createClassLoader(context,pluginApk,mNativeLibDir,context.getClassLoader());
        this.setClassLoader(mClassLoader);

        Application mApplication = makeApplication(false, mPluginManager.getInstrumentation());
        setApplication(mApplication);
    }

    protected Application makeApplication(boolean forceDefaultAppClass, Instrumentation instrumentation) throws Exception {

        String appClass = this.packageInfo.applicationInfo.className;
        if (forceDefaultAppClass || null == appClass) {
            appClass = "android.app.Application";
        }

        this.application = instrumentation.newApplication(this.classLoader, appClass, new PluginContext(mContext, this));
        // inject activityLifecycleCallbacks of the host application
//        mApplication.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksProxy());
        instrumentation.callApplicationOnCreate(this.application);
        return this.application;
    }


    protected ClassLoader createClassLoader(Context context, File apk, File libsDir, ClassLoader parent) throws Exception {
        File dexOutputDir = getDir(context, "dex");
        String dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader loader = new DexClassLoader(apk.getAbsolutePath(), dexOutputPath, libsDir.getAbsolutePath(), parent);

        if (true) {
            DexUtil.insertDex(loader, parent, libsDir);
        }

        return loader;
    }

    protected File getDir(Context context, String name) {
        return context.getDir(name, Context.MODE_PRIVATE);
    }

    protected Resources createResources(Context context, File apk) throws Exception {
        if (true) {
            return ResourcesManager.createResourcesSimple(context, apk.getAbsolutePath());
        } else {
            Resources hostResources = context.getResources();
            AssetManager assetManager = createAssetManager(context, apk);
            return new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
        }
    }

    protected AssetManager createAssetManager(Context context, File apk) throws Exception {
        AssetManager am = AssetManager.class.newInstance();
        Reflector.with(am).method("addAssetPath", String.class).call(apk.getAbsolutePath());
        return am;
    }

    /**
     * 获取cpu_abi
     * @return
     */
    public String getPhoneCPU(){
        String phoneInfo = android.os.Build.CPU_ABI;
        if(phoneInfo==null){
            phoneInfo = "armeabi";
        }
        mPhoneInfo = phoneInfo;
        return phoneInfo;
    }

    public String getPackageName() {
        return packageInfo.packageName;
    }

    public ActivityInfo findActivityByClassNameFromPkg(String actName) {
        if (actName.startsWith(".")) {
            actName = getPackageName() + actName;
        }
        if (packageInfo.activities == null) {
            return null;
        }
        for (ActivityInfo act : packageInfo.activities) {
            if(act.name.equals(actName)){
                return act;
            }
        }
        return null;
    }
    public ActivityInfo findActivityByClassName(String actName) {
        if (packageInfo.activities == null) {
            return null;
        }
        if (actName.startsWith(".")) {
            actName = getPackageName() + actName;
        }
        ResolveInfo act = activities.get(actName);
        if (act == null) {
            return null;
        }
        return act.activityInfo;
    }

    public void addActivity(ResolveInfo activity) {
        if (activities == null) {
            activities = new HashMap<String, ResolveInfo>(15);
        }
        fixActivityInfo(activity.activityInfo);
        activities.put(activity.activityInfo.name,activity);
        if (mainActivity == null && activity.filter != null
                && activity.filter.hasAction("android.intent.action.MAIN")
                && activity.filter.hasCategory("android.intent.category.LAUNCHER")
        ) {
            mainActivity = activity;
        }
    }

    private void fixActivityInfo(ActivityInfo activityInfo) {
        if (activityInfo != null) {
            if (activityInfo.name.startsWith(".")) {
                activityInfo.name = getPackageName() + activityInfo.name;
            }
        }
    }

    public ActivityInfo getActivityInfo(ComponentName componentName) {
        return this.mActivityInfos.get(componentName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
        activities = new HashMap<String, ResolveInfo>(packageInfo.activities.length);
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public AssetManager getAssets() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }


    public Collection<ResolveInfo> getActivities() {
        if (activities == null) {
            return null;
        }
        return activities.values();
    }

    public ResolveInfo getMainActivity() {
        return mainActivity;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public PluginManager getPluginManager() {
        return this.mPluginManager;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlugInfo other = (PlugInfo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "[ id=" + id + ", pkg=" + getPackageName()
                + " ]";
    }


}
