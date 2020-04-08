package com.info.aegis.plugindemo2;

import com.info.aegis.baselibrary.router.BaseManager;
import com.info.aegis.baselibrary.router.PluginModule;

/**
 * Created by leng on 2020/3/30.
 */
public class PluginModuleImpl implements PluginModule {

    @Override
    public void addPluginModule(BaseManager baseManager) {
        baseManager.addModule("12", new com.info.aegis.plugindemo2.router.PluginModule());
    }
}
