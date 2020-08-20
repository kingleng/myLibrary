package com.info.aegis.mypluginapplication;


import com.info.aegis.mypluginapplication.router.TestPluginModule;
import com.kingleng.baselibrary.router.BaseManager;
import com.kingleng.baselibrary.router.PluginModule;

/**
 * Created by leng on 2020/3/30.
 */
public class PluginModuleImpl implements PluginModule {

    @Override
    public void addPluginModule(BaseManager baseManager) {
        baseManager.addModule(Contents.RouterId, new TestPluginModule());
    }
}
