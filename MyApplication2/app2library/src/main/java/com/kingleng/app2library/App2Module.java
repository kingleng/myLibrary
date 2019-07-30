package com.kingleng.app2library;

import com.kingleng.baselibrary.base.BaseModule;
import com.kingleng.baselibrary.base.BaseRouter;

/**
 * Created by leng on 2019/7/26.
 */
public class App2Module extends BaseModule {

    @Override
    public BaseRouter onBind() {
        return new App2Router();
    }

}
