package com.info.aegis.mypluginapplication.plugin;

import com.info.aegis.baselibrary.router.BaseManager;
import com.info.aegis.baselibrary.router.PluginModule;

/**
 * Created by leng on 2020/3/30.
 */
public class PluginModuleImpl implements PluginModule {

    @Override
    public void addPluginModule(BaseManager baseManager) {
        baseManager.addModule("11", new com.info.aegis.mypluginapplication.plugin.router.PluginModule());
    }
}
