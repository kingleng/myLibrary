package com.info.aegis.mypluginapplication.router;


import com.kingleng.baselibrary.router.BaseModule;
import com.kingleng.baselibrary.router.BaseRouter;

/**
 * Created by leng on 2019/5/6.
 */
public class TestPluginModule extends BaseModule {

    @Override
    public BaseRouter onBind() {
        return new TestPluginRouter();
    }
}
