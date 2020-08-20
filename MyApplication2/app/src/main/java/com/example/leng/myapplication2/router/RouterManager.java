package com.example.leng.myapplication2.router;

import android.content.Context;
import android.util.Log;

import com.info.aegis.plugincorelibrary.PluginManager;
import com.kingleng.app2library.App2Module;
import com.kingleng.baselibrary.router.BaseManager;
import com.kingleng.baselibrary.router.Router;

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

    public void init(Context base){
        registerModule();
        Router.getInstance().init(getBaseModules());

        try {
            PluginManager.init(base);
        } catch (Exception e) {
            Log.e("RouterManager","插件框架启动失败！");
            e.printStackTrace();
        }
    }

    public void registerModule(){
        addModule("10",new AppModule());
        addModule("11",new App2Module());
    }

}
