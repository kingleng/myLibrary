package com.example.leng.myapplication2.router;

import com.kingleng.app2library.App2Module;

import com.kingleng.baselibrary.base.BaseManager;
import com.kingleng.baselibrary.base.Router;

/**
 * Created by leng on 2019/7/29.
 */
public class RouterManager extends BaseManager {

    private static RouterManager instance;

    public static RouterManager getInstance(){
        if(instance == null){
            instance = new RouterManager();
        }
        return instance;
    }

    public void init(){
        registerModule();
        Router.getInstance().init(getBaseModules());
    }

    public void registerModule(){
        addModule("10",new AppModule());
        addModule("11",new App2Module());
    }

}
