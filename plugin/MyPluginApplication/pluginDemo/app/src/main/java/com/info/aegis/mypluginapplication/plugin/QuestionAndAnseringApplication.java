package com.info.aegis.mypluginapplication.plugin;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.info.aegis.baselibrary.base.IComponentApplication;

/**
 * Created by leng on 2019/12/23.
 */
public class QuestionAndAnseringApplication implements IComponentApplication {

    @Override
    public void init(Application application) {


        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(application, SpeechConstant.APPID +"=5db11808");
    }


}
