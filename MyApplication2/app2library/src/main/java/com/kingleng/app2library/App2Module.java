package com.kingleng.app2library;

import com.kingleng.baselibrary.router.BaseModule;
import com.kingleng.baselibrary.router.BaseRouter;

/**
 * Created by leng on 2019/7/26.
 */
public class App2Module extends BaseModule {

    @Override
    public BaseRouter onBind() {
        return new App2Router();
    }

}
