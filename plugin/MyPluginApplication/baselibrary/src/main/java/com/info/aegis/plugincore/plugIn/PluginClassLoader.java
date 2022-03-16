package com.info.aegis.plugincore.plugIn;

import dalvik.system.DexClassLoader;

/**
 * Created by leng on 2020/3/12.
 */
public class PluginClassLoader extends DexClassLoader {

    PlugInfo plugInfo;

    public PluginClassLoader(PlugInfo plugInfo, String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
        this.plugInfo = plugInfo;
    }

    public PlugInfo getPlugInfo() {
        return plugInfo;
    }
}
