package com.example.leng.myapplication2.router;

import com.kingleng.baselibrary.router.BaseModule;
import com.kingleng.baselibrary.router.BaseRouter;

/**
 * Created by leng on 2019/5/6.
 */
public class AppModule extends BaseModule {

    @Override
    public BaseRouter onBind() {
        return new AppRouter();
    }
}
