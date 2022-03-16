package com.info.aegis.plugincorelibrary.plugin;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;


/**
 * @author lj
 * @version 1.0
 */
public class PluginContext extends LayoutInflaterProxyContext {

    private PlugInfo plugInfo;

    public PluginContext(Context hostContext, PlugInfo plugInfo) {
        super(hostContext);
        if (plugInfo == null) {
            throw new IllegalStateException("Create a plugin context, but not given host context!");
        }
        this.plugInfo = plugInfo;
    }

    public PluginContext(PlugInfo plugin) {
        super(plugin.getPluginManager().getHostContext());
        this.plugInfo = plugin;
    }

    @Override
    public Resources getResources() {
        return plugInfo.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return plugInfo.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return plugInfo.getClassLoader();
    }

    @Override
    public PackageManager getPackageManager() {
        return this.plugInfo.getPackageManager();
    }

    @Override
    public String getPackageName() {
        return super.getPackageName();
    }

    @Override
    public Context getBaseContext() {
        return getBaseContextInner(super.getBaseContext());
    }

    private Context getBaseContextInner(Context baseContext) {
        if (baseContext instanceof ContextWrapper) {
            return getBaseContextInner(((ContextWrapper) baseContext).getBaseContext());
        }
        return baseContext;
    }
}
