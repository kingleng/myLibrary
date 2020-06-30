package com.info.aegis.plugincore.plugIn;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ChangedPackages;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.VersionedPackage;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.info.aegis.plugincore.PluginManager;
import com.info.aegis.plugincore.utils.DexUtil;
import com.info.aegis.plugincore.utils.PackageParserCompat;
import com.info.aegis.plugincore.utils.Reflector;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexClassLoader;

import static com.info.aegis.plugincore.utils.DexUtil.NATIVE_DIR;

/**
 * Created by leng on 2020/3/12.
 * 插件信息类
 */
public class PlugInfo {

    public static final String TAG = "PlugInfo";

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
    protected Context mPluginContext;
    protected PluginPackageManager mPackageManager;

    private PluginManager mPluginManager;
    protected final PackageParser.Package mPackage;
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

        this.mPackage = PackageParserCompat.parsePackage(context, pluginApk, PackageParser.PARSE_MUST_BE_APK);
        this.mPackage.applicationInfo.metaData = this.mPackage.mAppMetaData;

        //Load Plugin Manifest
        //获取一些apk里面 MainFest.xml的一些内容，包括 pacageName 、Activity 、application 等
        PluginManifestUtil.setManifestInfo(context, dexPath, this,mPhoneInfo);

        if (pluginManager.getLoadedPlugin(packageInfo.packageName) != null) {
            throw new RuntimeException("plugin has already been loaded : " + packageInfo.packageName);
        }

        this.mPackageManager = createPluginPackageManager();
        this.mPluginContext = createPluginContext(null);

        // Cache activities
        Map<ComponentName, ActivityInfo> activityInfos = new HashMap<ComponentName, ActivityInfo>();
        for (PackageParser.Activity activity : this.mPackage.activities) {
            activity.info.metaData = activity.metaData;
            activityInfos.put(activity.getComponentName(), activity.info);
        }
        this.mActivityInfos = Collections.unmodifiableMap(activityInfos);

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

    protected PluginPackageManager createPluginPackageManager() {
        return new PluginPackageManager();
    }

    public PluginContext createPluginContext(Context context) {
        if (context == null) {
            return new PluginContext(this);
        }
        return new PluginContext(context,this);
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

    public ApplicationInfo getApplicationInfo() {
        return this.mPackage.applicationInfo;
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

    public PackageManager getPackageManager() {
        return this.mPackageManager;
    }

    public ResolveInfo resolveActivity(Intent intent, int flags) {
        List<ResolveInfo> query = this.queryIntentActivities(intent, flags);
        if (null == query || query.isEmpty()) {
            return null;
        }

        ContentResolver resolver = this.mPluginContext.getContentResolver();
        return chooseBestActivity(intent, intent.resolveTypeIfNeeded(resolver), flags, query);
    }

    protected ResolveInfo chooseBestActivity(Intent intent, String s, int flags, List<ResolveInfo> query) {
        return query.get(0);
    }

    public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
        ComponentName component = intent.getComponent();
        List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();
        ContentResolver resolver = this.mPluginContext.getContentResolver();

        for (PackageParser.Activity activity : this.mPackage.activities) {
            if (match(activity, component)) {
                ResolveInfo resolveInfo = new ResolveInfo();
                resolveInfo.activityInfo = activity.info;
                resolveInfos.add(resolveInfo);
            } else if (component == null) {
                // only match implicit intent
                for (PackageParser.ActivityIntentInfo intentInfo : activity.intents) {
                    if (intentInfo.match(resolver, intent, true, TAG) >= 0) {
                        ResolveInfo resolveInfo = new ResolveInfo();
                        resolveInfo.activityInfo = activity.info;
                        resolveInfos.add(resolveInfo);
                        break;
                    }
                }
            }
        }

        return resolveInfos;
    }

    protected boolean match(PackageParser.Component component, ComponentName target) {
        ComponentName source = component.getComponentName();
        if (source == target) return true;
        if (source != null && target != null
                && source.getClassName().equals(target.getClassName())
                && (source.getPackageName().equals(target.getPackageName())
                || mContext.getPackageName().equals(target.getPackageName()))) {
            return true;
        }
        return false;
    }

    /**
     * @author johnsonlee
     */
    protected class PluginPackageManager extends PackageManager {

        protected PackageManager mHostPackageManager = mContext.getPackageManager();

        @Override
        public PackageInfo getPackageInfo(String packageName, int flags) throws NameNotFoundException {

            PlugInfo plugin = mPluginManager.getLoadedPlugin(packageName);
            if (null != plugin) {
                return plugin.packageInfo;
            }

            return this.mHostPackageManager.getPackageInfo(packageName, flags);
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public PackageInfo getPackageInfo(VersionedPackage versionedPackage, int i) throws NameNotFoundException {

            PlugInfo plugin = mPluginManager.getLoadedPlugin(versionedPackage.getPackageName());
            if (null != plugin) {
                return plugin.packageInfo;
            }

            return this.mHostPackageManager.getPackageInfo(versionedPackage, i);
        }

        @Override
        public String[] currentToCanonicalPackageNames(String[] names) {
            return this.mHostPackageManager.currentToCanonicalPackageNames(names);
        }

        @Override
        public String[] canonicalToCurrentPackageNames(String[] names) {
            return this.mHostPackageManager.canonicalToCurrentPackageNames(names);
        }

        @Override
        public Intent getLaunchIntentForPackage(@NonNull String packageName) {
            return this.mHostPackageManager.getLaunchIntentForPackage(packageName);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public Intent getLeanbackLaunchIntentForPackage(@NonNull String packageName) {
            return this.mHostPackageManager.getLeanbackLaunchIntentForPackage(packageName);
        }

        @Override
        public int[] getPackageGids(@NonNull String packageName) throws NameNotFoundException {
            return this.mHostPackageManager.getPackageGids(packageName);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public int[] getPackageGids(String packageName, int flags) throws NameNotFoundException {
            return this.mHostPackageManager.getPackageGids(packageName, flags);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public int getPackageUid(String packageName, int flags) throws NameNotFoundException {
            return this.mHostPackageManager.getPackageUid(packageName, flags);
        }

        @Override
        public PermissionInfo getPermissionInfo(String name, int flags) throws NameNotFoundException {
            return this.mHostPackageManager.getPermissionInfo(name, flags);
        }

        @Override
        public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) throws NameNotFoundException {
            return this.mHostPackageManager.queryPermissionsByGroup(group, flags);
        }

        @Override
        public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws NameNotFoundException {
            return this.mHostPackageManager.getPermissionGroupInfo(name, flags);
        }

        @Override
        public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
            return this.mHostPackageManager.getAllPermissionGroups(flags);
        }

        @Override
        public ApplicationInfo getApplicationInfo(String packageName, int flags) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(packageName);
            if (null != plugin) {
                return plugin.getApplicationInfo();
            }

            return this.mHostPackageManager.getApplicationInfo(packageName, flags);
        }

        @Override
        public ActivityInfo getActivityInfo(ComponentName component, int flags) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(component);
            if (null != plugin) {
                return plugin.mActivityInfos.get(component);
            }

            return this.mHostPackageManager.getActivityInfo(component, flags);
        }

        @Override
        public ActivityInfo getReceiverInfo(ComponentName component, int flags) throws NameNotFoundException {
            return this.mHostPackageManager.getReceiverInfo(component, flags);
        }

        @Override
        public ServiceInfo getServiceInfo(ComponentName component, int flags) throws NameNotFoundException {
            return this.mHostPackageManager.getServiceInfo(component, flags);
        }

        @Override
        public ProviderInfo getProviderInfo(ComponentName component, int flags) throws NameNotFoundException {
            return this.mHostPackageManager.getProviderInfo(component, flags);
        }

        @Override
        public List<PackageInfo> getInstalledPackages(int flags) {
            return this.mHostPackageManager.getInstalledPackages(flags);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public List<PackageInfo> getPackagesHoldingPermissions(String[] permissions, int flags) {
            return this.mHostPackageManager.getPackagesHoldingPermissions(permissions, flags);
        }

        @Override
        public int checkPermission(String permName, String pkgName) {
            return this.mHostPackageManager.checkPermission(permName, pkgName);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public boolean isPermissionRevokedByPolicy(@NonNull String permName, @NonNull String pkgName) {
            return this.mHostPackageManager.isPermissionRevokedByPolicy(permName, pkgName);
        }

        @Override
        public boolean addPermission(PermissionInfo info) {
            return this.mHostPackageManager.addPermission(info);
        }

        @Override
        public boolean addPermissionAsync(PermissionInfo info) {
            return this.mHostPackageManager.addPermissionAsync(info);
        }

        @Override
        public void removePermission(String name) {
            this.mHostPackageManager.removePermission(name);
        }

        @Override
        public int checkSignatures(String pkg1, String pkg2) {
            return this.mHostPackageManager.checkSignatures(pkg1, pkg2);
        }

        @Override
        public int checkSignatures(int uid1, int uid2) {
            return this.mHostPackageManager.checkSignatures(uid1, uid2);
        }

        @Override
        public String[] getPackagesForUid(int uid) {
            return this.mHostPackageManager.getPackagesForUid(uid);
        }

        @Override
        public String getNameForUid(int uid) {
            return this.mHostPackageManager.getNameForUid(uid);
        }

        @Override
        public List<ApplicationInfo> getInstalledApplications(int flags) {
            return this.mHostPackageManager.getInstalledApplications(flags);
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public boolean isInstantApp() {
            return this.mHostPackageManager.isInstantApp();
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public boolean isInstantApp(String packageName) {
            return this.mHostPackageManager.isInstantApp(packageName);
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public int getInstantAppCookieMaxBytes() {
            return this.mHostPackageManager.getInstantAppCookieMaxBytes();
        }

        @TargetApi(Build.VERSION_CODES.O)
        @NonNull
        @Override
        public byte[] getInstantAppCookie() {
            return this.mHostPackageManager.getInstantAppCookie();
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public void clearInstantAppCookie() {
            this.mHostPackageManager.clearInstantAppCookie();
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public void updateInstantAppCookie(@Nullable byte[] cookie) {
            this.mHostPackageManager.updateInstantAppCookie(cookie);
        }

        @Override
        public String[] getSystemSharedLibraryNames() {
            return this.mHostPackageManager.getSystemSharedLibraryNames();
        }

        @TargetApi(Build.VERSION_CODES.O)
        @NonNull
        @Override
        public List<SharedLibraryInfo> getSharedLibraries(int flags) {
            return this.mHostPackageManager.getSharedLibraries(flags);
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Nullable
        @Override
        public ChangedPackages getChangedPackages(int sequenceNumber) {
            return this.mHostPackageManager.getChangedPackages(sequenceNumber);
        }

        @Override
        public FeatureInfo[] getSystemAvailableFeatures() {
            return this.mHostPackageManager.getSystemAvailableFeatures();
        }

        @Override
        public boolean hasSystemFeature(String name) {
            return this.mHostPackageManager.hasSystemFeature(name);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean hasSystemFeature(String name, int version) {
            return this.mHostPackageManager.hasSystemFeature(name, version);
        }

        @Override
        public ResolveInfo resolveActivity(Intent intent, int flags) {
            ResolveInfo resolveInfo = mPluginManager.resolveActivity(intent, flags);
            if (null != resolveInfo) {
                return resolveInfo;
            }

            return this.mHostPackageManager.resolveActivity(intent, flags);
        }

        @Override
        public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
            ComponentName component = intent.getComponent();
            if (null == component) {
                if (intent.getSelector() != null) {
                    intent = intent.getSelector();
                    component = intent.getComponent();
                }
            }

            if (null != component) {
                PlugInfo plugin = mPluginManager.getLoadedPlugin(component);
                if (null != plugin) {
                    ActivityInfo activityInfo = plugin.getActivityInfo(component);
                    if (activityInfo != null) {
                        ResolveInfo resolveInfo = new ResolveInfo();
                        resolveInfo.activityInfo = activityInfo;
                        return Arrays.asList(resolveInfo);
                    }
                }
            }

            List<ResolveInfo> all = new ArrayList<ResolveInfo>();

            List<ResolveInfo> pluginResolveInfos = mPluginManager.queryIntentActivities(intent, flags);
            if (null != pluginResolveInfos && pluginResolveInfos.size() > 0) {
                all.addAll(pluginResolveInfos);
            }

            List<ResolveInfo> hostResolveInfos = this.mHostPackageManager.queryIntentActivities(intent, flags);
            if (null != hostResolveInfos && hostResolveInfos.size() > 0) {
                all.addAll(hostResolveInfos);
            }

            return all;
        }

        @Override
        public List<ResolveInfo> queryIntentActivityOptions(ComponentName caller, Intent[] specifics, Intent intent, int flags) {
            return this.mHostPackageManager.queryIntentActivityOptions(caller, specifics, intent, flags);
        }

        @Override
        public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int flags) {
            return this.mHostPackageManager.queryBroadcastReceivers(intent, flags);
        }

        @Override
        public ResolveInfo resolveService(Intent intent, int flags) {
            return this.mHostPackageManager.resolveService(intent, flags);
        }

        @Override
        public List<ResolveInfo> queryIntentServices(Intent intent, int flags) {
            return this.queryIntentServices(intent,flags);
        }

        @Override
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public List<ResolveInfo> queryIntentContentProviders(Intent intent, int flags) {
            return this.mHostPackageManager.queryIntentContentProviders(intent, flags);
        }

        @Override
        public ProviderInfo resolveContentProvider(String name, int flags) {
            return this.mHostPackageManager.resolveContentProvider(name, flags);
        }

        @Override
        public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags) {
            return this.mHostPackageManager.queryContentProviders(processName, uid, flags);
        }

        @Override
        public InstrumentationInfo getInstrumentationInfo(ComponentName component, int flags) throws NameNotFoundException {
            return this.mHostPackageManager.getInstrumentationInfo(component, flags);
        }

        @Override
        public List<InstrumentationInfo> queryInstrumentation(String targetPackage, int flags) {
            return this.mHostPackageManager.queryInstrumentation(targetPackage, flags);
        }

        @Override
        public Drawable getDrawable(String packageName, int resid, ApplicationInfo appInfo) {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(packageName);
            if (null != plugin) {
                return plugin.resources.getDrawable(resid);
            }

            return this.mHostPackageManager.getDrawable(packageName, resid, appInfo);
        }

        @Override
        public Drawable getActivityIcon(ComponentName component) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(component);
            if (null != plugin) {
                return plugin.resources.getDrawable(plugin.mActivityInfos.get(component).icon);
            }

            return this.mHostPackageManager.getActivityIcon(component);
        }

        @Override
        public Drawable getActivityIcon(Intent intent) throws NameNotFoundException {
//            ResolveInfo ri = mPluginManager.resolveActivity(intent);
//            if (null != ri) {
//                LoadedPlugin plugin = mPluginManager.getLoadedPlugin(ri.resolvePackageName);
//                return plugin.mResources.getDrawable(ri.activityInfo.icon);
//            }

            return this.mHostPackageManager.getActivityIcon(intent);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
        @Override
        public Drawable getActivityBanner(ComponentName component) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(component);
            if (null != plugin) {
                return plugin.resources.getDrawable(plugin.mActivityInfos.get(component).banner);
            }

            return this.mHostPackageManager.getActivityBanner(component);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
        @Override
        public Drawable getActivityBanner(Intent intent) throws NameNotFoundException {
            ResolveInfo ri = mPluginManager.resolveActivity(intent);
            if (null != ri) {
                PlugInfo plugin = mPluginManager.getLoadedPlugin(ri.resolvePackageName);
                return plugin.resources.getDrawable(ri.activityInfo.banner);
            }

            return this.mHostPackageManager.getActivityBanner(intent);
        }

        @Override
        public Drawable getDefaultActivityIcon() {
            return this.mHostPackageManager.getDefaultActivityIcon();
        }

        @Override
        public Drawable getApplicationIcon(ApplicationInfo info) {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(info.packageName);
            if (null != plugin) {
                return plugin.resources.getDrawable(info.icon);
            }

            return this.mHostPackageManager.getApplicationIcon(info);
        }

        @Override
        public Drawable getApplicationIcon(String packageName) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(packageName);
            if (null != plugin) {
                return plugin.resources.getDrawable(plugin.mPackage.applicationInfo.icon);
            }

            return this.mHostPackageManager.getApplicationIcon(packageName);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
        @Override
        public Drawable getApplicationBanner(ApplicationInfo info) {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(info.packageName);
            if (null != plugin) {
                return plugin.resources.getDrawable(info.banner);
            }

            return this.mHostPackageManager.getApplicationBanner(info);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
        @Override
        public Drawable getApplicationBanner(String packageName) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(packageName);
            if (null != plugin) {
                return plugin.resources.getDrawable(plugin.mPackage.applicationInfo.banner);
            }

            return this.mHostPackageManager.getApplicationBanner(packageName);
        }

        @Override
        public Drawable getActivityLogo(ComponentName component) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(component);
            if (null != plugin) {
                return plugin.resources.getDrawable(plugin.mActivityInfos.get(component).logo);
            }

            return this.mHostPackageManager.getActivityLogo(component);
        }

        @Override
        public Drawable getActivityLogo(Intent intent) throws NameNotFoundException {
            ResolveInfo ri = mPluginManager.resolveActivity(intent);
            if (null != ri) {
                PlugInfo plugin = mPluginManager.getLoadedPlugin(ri.resolvePackageName);
                return plugin.resources.getDrawable(ri.activityInfo.logo);
            }

            return this.mHostPackageManager.getActivityLogo(intent);
        }

        @Override
        public Drawable getApplicationLogo(ApplicationInfo info) {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(info.packageName);
            if (null != plugin) {
                return plugin.resources.getDrawable(0 != info.logo ? info.logo : android.R.drawable.sym_def_app_icon);
            }

            return this.mHostPackageManager.getApplicationLogo(info);
        }

        @Override
        public Drawable getApplicationLogo(String packageName) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(packageName);
            if (null != plugin) {
                return plugin.resources.getDrawable(0 != plugin.mPackage.applicationInfo.logo ? plugin.mPackage.applicationInfo.logo : android.R.drawable.sym_def_app_icon);
            }

            return this.mHostPackageManager.getApplicationLogo(packageName);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public Drawable getUserBadgedIcon(Drawable icon, UserHandle user) {
            return this.mHostPackageManager.getUserBadgedIcon(icon, user);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public Drawable getUserBadgeForDensity(UserHandle user, int density) {
            try {
                return Reflector.with(this.mHostPackageManager)
                        .method("getUserBadgeForDensity", UserHandle.class, int.class)
                        .call(user, density);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public Drawable getUserBadgedDrawableForDensity(Drawable drawable, UserHandle user, Rect badgeLocation, int badgeDensity) {
            return this.mHostPackageManager.getUserBadgedDrawableForDensity(drawable, user, badgeLocation, badgeDensity);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public CharSequence getUserBadgedLabel(CharSequence label, UserHandle user) {
            return this.mHostPackageManager.getUserBadgedLabel(label, user);
        }

        @Override
        public CharSequence getText(String packageName, int resid, ApplicationInfo appInfo) {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(packageName);
            if (null != plugin) {
                return plugin.resources.getText(resid);
            }

            return this.mHostPackageManager.getText(packageName, resid, appInfo);
        }

        @Override
        public XmlResourceParser getXml(String packageName, int resid, ApplicationInfo appInfo) {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(packageName);
            if (null != plugin) {
                return plugin.resources.getXml(resid);
            }

            return this.mHostPackageManager.getXml(packageName, resid, appInfo);
        }

        @Override
        public CharSequence getApplicationLabel(ApplicationInfo info) {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(info.packageName);
            if (null != plugin) {
                try {
                    return plugin.resources.getText(info.labelRes);
                } catch (Resources.NotFoundException e) {
                    // ignored.
                }
            }

            return this.mHostPackageManager.getApplicationLabel(info);
        }

        @Override
        public Resources getResourcesForActivity(ComponentName component) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(component);
            if (null != plugin) {
                return plugin.resources;
            }

            return this.mHostPackageManager.getResourcesForActivity(component);
        }

        @Override
        public Resources getResourcesForApplication(ApplicationInfo app) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(app.packageName);
            if (null != plugin) {
                return plugin.resources;
            }

            return this.mHostPackageManager.getResourcesForApplication(app);
        }

        @Override
        public Resources getResourcesForApplication(String appPackageName) throws NameNotFoundException {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(appPackageName);
            if (null != plugin) {
                return plugin.resources;
            }

            return this.mHostPackageManager.getResourcesForApplication(appPackageName);
        }

        @Override
        public void verifyPendingInstall(int id, int verificationCode) {
            this.mHostPackageManager.verifyPendingInstall(id, verificationCode);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) {
            this.mHostPackageManager.extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay);
        }

        @Override
        public void setInstallerPackageName(String targetPackage, String installerPackageName) {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(targetPackage);
            if (null != plugin) {
                return;
            }

            this.mHostPackageManager.setInstallerPackageName(targetPackage, installerPackageName);
        }

        @Override
        public String getInstallerPackageName(String packageName) {
            PlugInfo plugin = mPluginManager.getLoadedPlugin(packageName);
            if (null != plugin) {
                return mContext.getPackageName();
            }

            return this.mHostPackageManager.getInstallerPackageName(packageName);
        }

        @Override
        public void addPackageToPreferred(String packageName) {
            this.mHostPackageManager.addPackageToPreferred(packageName);
        }

        @Override
        public void removePackageFromPreferred(String packageName) {
            this.mHostPackageManager.removePackageFromPreferred(packageName);
        }

        @Override
        public List<PackageInfo> getPreferredPackages(int flags) {
            return this.mHostPackageManager.getPreferredPackages(flags);
        }

        @Override
        public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity) {
            this.mHostPackageManager.addPreferredActivity(filter, match, set, activity);
        }

        @Override
        public void clearPackagePreferredActivities(String packageName) {
            this.mHostPackageManager.clearPackagePreferredActivities(packageName);
        }

        @Override
        public int getPreferredActivities(@NonNull List<IntentFilter> outFilters, @NonNull List<ComponentName> outActivities, String packageName) {
            return this.mHostPackageManager.getPreferredActivities(outFilters, outActivities, packageName);
        }

        @Override
        public void setComponentEnabledSetting(ComponentName component, int newState, int flags) {
            this.mHostPackageManager.setComponentEnabledSetting(component, newState, flags);
        }

        @Override
        public int getComponentEnabledSetting(ComponentName component) {
            return this.mHostPackageManager.getComponentEnabledSetting(component);
        }

        @Override
        public void setApplicationEnabledSetting(String packageName, int newState, int flags) {
            this.mHostPackageManager.setApplicationEnabledSetting(packageName, newState, flags);
        }

        @Override
        public int getApplicationEnabledSetting(String packageName) {
            return this.mHostPackageManager.getApplicationEnabledSetting(packageName);
        }

        @Override
        public boolean isSafeMode() {
            return this.mHostPackageManager.isSafeMode();
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public void setApplicationCategoryHint(@NonNull String packageName, int categoryHint) {
            this.mHostPackageManager.setApplicationCategoryHint(packageName, categoryHint);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public @NonNull
        PackageInstaller getPackageInstaller() {
            return this.mHostPackageManager.getPackageInstaller();
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public boolean canRequestPackageInstalls() {
            return this.mHostPackageManager.canRequestPackageInstalls();
        }

        public Drawable loadItemIcon(PackageItemInfo itemInfo, ApplicationInfo appInfo) {
            if (itemInfo == null) {
                return null;
            }
            return itemInfo.loadIcon(this.mHostPackageManager);
        }
    }


}
