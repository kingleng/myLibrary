package com.info.aegis.mypluginapplication.router;

import android.content.Context;
import android.util.Log;

import com.info.aegis.baselibrary.router.BaseManager;
import com.info.aegis.baselibrary.router.Router;
import com.info.aegis.plugincore.PluginManager;

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
        //主模块
        addModule("10",new AppModule());
    }

}
