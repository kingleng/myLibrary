package com.info.aegis.mypluginapplication;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.info.aegis.baselibrary.TopApplication;
import com.info.aegis.baselibrary.base.IComponentApplication;
import com.info.aegis.mypluginapplication.router.RouterManager;

/**
 * Created by leng on 2020/3/19.
 */
public class MyApplication extends TopApplication {

    private static final String[] MODULESLIST =
            {"com.info.aegis.msglibrary.JudgeMsgApplication",
                    "com.info.aegis.fxpglibrary.RiskAssessmentApplication",
                    "com.info.aegis.znwdlibrary.QuestionAndAnseringApplication"};


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        //路由初始化
        RouterManager.getInstance().init(base);

        //Module类的APP初始化
//        modulesApplicationInit();
    }

    private void modulesApplicationInit(){
        for (String moduleImpl : MODULESLIST){
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof IComponentApplication){
                    ((IComponentApplication) obj).init(MyApplication.this);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

}
