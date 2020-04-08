package com.info.aegis.plugindemo2.router;

import com.info.aegis.baselibrary.router.BaseModule;
import com.info.aegis.baselibrary.router.BaseRouter;

/**
 * Created by leng on 2019/5/6.
 */
public class PluginModule extends BaseModule {

    @Override
    public BaseRouter onBind() {
        return new PluginRouter();
    }
}
