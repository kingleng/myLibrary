package com.kingleng.baselibrary.router;

import android.content.Context;
import android.os.Bundle;

import com.kingleng.baselibrary.router.interceptor.InterceptorManager;

import java.util.Map;
import java.util.Set;

/**
 * Created by leng on 2019/5/6.
 */
public class Router {

    Map<String, BaseModule> baseModules;

    public void init(Map<String, BaseModule> baseModules) {
        this.baseModules = baseModules;
    }

    private static Router instance;

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public void startActivityByUrl(Context mContext, String url, Bundle b) {

        if (!url.contains("adType")) {
            BaseModule module = baseModules.get("10");
            Bundle bundle = new Bundle();
            if (b != null) {
                bundle = b;
            }
            String[] pa = url.split("&&");
            for (int i = 0; i < pa.length; i++) {
                if (i == 0) {
                    bundle.putString("url", pa[0]);
                } else {
                    String[] kv = pa[i].split("=");
                    if (kv.length >= 2) {
                        bundle.putString(kv[0], kv[1]);
                    }
                }

            }

            module.startActivity(mContext, TypeManager.MIAN_ITEM_0, bundle);
            return;
        }
//        String params = url.split("\\?")[1];
        String params = url.replace("www.aegis.com?", "");
        params = params.replace("https://www.aegis.com?", "");
        params = params.replace("http://www.aegis.com?", "");
        String adType = params.split("&")[0].replace("adType=", "");

        boolean mInterceptor = InterceptorManager.getInstance().Interceptor(adType);
        if(mInterceptor){
            return;
        }

        Set<String> keys = baseModules.keySet();
        boolean isUsed = false;
        for (String key : keys) {
            if (adType.startsWith(key)) {
                BaseModule module = baseModules.get(key);
                Bundle bundle = new Bundle();
                if (b != null) {
                    bundle = b;
                }
                String[] pa = params.split("&");
                for (String pp : pa) {
                    String[] kv = pp.split("=");
                    bundle.putString(kv[0], kv[1]);
                }
                isUsed = module.startActivity(mContext, adType, bundle);
                break;
            }
        }

        if (!isUsed) {
            BaseModule module = baseModules.get("10");
            Bundle bundle = new Bundle();
            bundle.putString("toast", "页面找不到了！");
            module.startActivity(mContext, TypeManager.MIAN_ITEM_1, bundle);
        }

    }


}
