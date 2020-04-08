package com.info.aegis.baselibrary.net;

import java.util.Map;

/**
 * Created by leng on 2019/11/21.
 */
public class BaseTask {

    public Object mTag;
    public Map<String, String> params;
    public String url;
    public String method;
    public NetworkCallBack networkCallBack;

    public boolean isJson = false;
    public String json;


    public BaseTask(String url, String method, Map<String, String> params, Object mTag, NetworkCallBack networkCallBack) {
        this.mTag = mTag;
        this.params = params;
        this.url = url;
        this.method = method;
        this.networkCallBack = networkCallBack;
    }

    public BaseTask(String url, String method, String json, Object mTag, NetworkCallBack networkCallBack) {
        this.mTag = mTag;
        this.json = json;
        this.url = url;
        this.method = method;
        this.networkCallBack = networkCallBack;
        isJson = true;
    }


}
