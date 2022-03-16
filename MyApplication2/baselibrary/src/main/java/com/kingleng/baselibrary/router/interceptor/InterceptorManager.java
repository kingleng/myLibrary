package com.kingleng.baselibrary.router.interceptor;

import com.kingleng.baselibrary.router.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leng on 2021/1/11.
 */
public class InterceptorManager {

    List<BaseInterceptor> interceptorList = new ArrayList<>();

    private static InterceptorManager instance;

    public static InterceptorManager getInstance() {
        if (instance == null) {
            instance = new InterceptorManager();
        }
        return instance;
    }

    public void init(){
        interceptorList.add(new LoginInterceptor());
    }

    public void addInterceptor(BaseInterceptor interceptor){

        for(int i=0;i<interceptorList.size();i++){
            if(interceptor.priority<interceptorList.get(i).priority){
                interceptorList.add(i,interceptor);
                return;
            }
        }
        interceptorList.add(interceptor);
    }

    public boolean Interceptor(String adType){
        for(BaseInterceptor interceptor: interceptorList){
            if(interceptor.interceptor(adType)){
                return true;
            }
        }
        return false;
    }
}
