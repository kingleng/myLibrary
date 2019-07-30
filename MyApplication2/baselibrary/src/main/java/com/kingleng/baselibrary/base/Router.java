package com.kingleng.baselibrary.base;

import android.content.Context;
import android.os.Bundle;

import java.util.Map;
import java.util.Set;

/**
 * Created by leng on 2019/5/6.
 */
public class Router {

    Map<String,BaseModule> baseModules;

    public void init(Map<String,BaseModule> baseModules){
        this.baseModules = baseModules;
    }

    private static Router instance;

    public static Router getInstance(){
        if(instance == null){
            instance = new Router();
        }
        return instance;
    }

    public void startActivityByUrl(Context mContext, String url){

        if(!url.contains("adType")){
//            Intent intent = new Intent(mContext, WebviewActivity.class);
//            intent.putExtra("url",url);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
            BaseModule module = baseModules.get("10");
            Bundle bundle = new Bundle();
            bundle.putString("url",url);
            module.startActivity(mContext,"100000",bundle);
            return;
        }
        String params = url.split("\\?")[1];
        String adType = params.split("&")[0].replace("adType=","");

//        Map<String,BaseModule> baseModules = RouterManager.getInstance().getBaseModules();
        Set<String> keys = baseModules.keySet();

        boolean isUsed = false;
        for(String key:keys){
            if(adType.contains(key)){
                BaseModule module = baseModules.get(key);
                Bundle bundle = new Bundle();
                isUsed = module.startActivity(mContext,adType,bundle);
                break;
            }
        }

        if(!isUsed){
            BaseModule module = baseModules.get("10");
            Bundle bundle = new Bundle();
            bundle.putString("toast","页面找不到了！");
            module.startActivity(mContext,"1000",bundle);
        }

    }




}
