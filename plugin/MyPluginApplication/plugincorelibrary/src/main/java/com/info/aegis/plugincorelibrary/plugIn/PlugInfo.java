package com.info.aegis.plugincorelibrary.plugIn;

import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leng on 2020/3/12.
 * 插件信息类
 */
public class PlugInfo implements Serializable {

    private String id;
    private String filePath;
    private PackageInfo packageInfo;
    private Map<String, ResolveInfo> activities;
    private ResolveInfo mainActivity;

    private transient ClassLoader classLoader;
    private transient Application application;
    private transient AssetManager assetManager;
    private transient Resources resources;

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
