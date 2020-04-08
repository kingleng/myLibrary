package com.info.aegis.baselibrary.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.info.aegis.baselibrary.event.OnlineEvent;
import com.info.aegis.baselibrary.net.BaseTask;
import com.info.aegis.baselibrary.net.NetworkCallBack;
import com.info.aegis.baselibrary.net.OKHttpHelper;
import com.info.aegis.baselibrary.utils.StatisticUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by leng on 2019/9/9.
 */
public class LibraryActivity extends Activity {

    public boolean isOnline = false;

    public StatisticUtils statisticUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statisticUtils = new StatisticUtils(this);
    }

    public void setStatisticCode(String code){
        String[] params = code.split("/");
        statisticUtils.setCode(params[0],params[1],params[2],params[3],System.currentTimeMillis());
    }

    public void setStatisticArticle(String code){
        String[] params = code.split("/");
        statisticUtils.setArticleCode(params[0],params[1],params[2],params[3],System.currentTimeMillis(),params[4]);
    }

    @Override
    protected void onStart() {
        super.onStart();
        statisticUtils.reset();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        OKHttpHelper.getInstance(this).cancelAll();
    }

    @Override
    protected void onStop() {
        super.onStop();
        statisticUtils.sendData();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    Toast toast;
    public void toastCenter(String msg){
        if(toast!=null){
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    //数据更新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnlineEvent event) {
        isOnline = event.isOnline;
    }

    public void requstInfo(String url, NetMsgListener listener){
        requstInfo(url,null, listener);
    }

    public void requstPostInfo(String url, NetMsgListener listener){
        requstInfo(url,null,"post", listener);
    }

    public void requstInfo(String url, Map<String,String> params, NetMsgListener listener){
        requstInfo(url,params,"get", listener);
    }

    public void requstInfoByJson(String url, String json, String method, NetMsgListener listener){

        BaseTask baseTask = new BaseTask(url,
                (TextUtils.equals(method, "post")) ? OKHttpHelper.ReqMethod.POST : OKHttpHelper.ReqMethod.GET,
                json,
                url,
                new NetworkCallBack() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        if (listener != null) {
                            listener.onSucceed(jsonObject);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        if (listener != null) {
                            listener.onFailed(error);
                        }
                    }

                });

        OKHttpHelper.getInstance(this)
                .executeNetTask(baseTask);

    }

    public void requstInfo(String url, Map<String,String> params, String method, NetMsgListener listener){

        BaseTask baseTask = new BaseTask(url,
                (TextUtils.equals(method, "post")) ? OKHttpHelper.ReqMethod.POST : OKHttpHelper.ReqMethod.GET,
                params,
                url,
                new NetworkCallBack() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        if (listener != null) {
                            listener.onSucceed(jsonObject);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        if (listener != null) {
                            listener.onFailed(error);
                        }
                    }

                });

        OKHttpHelper.getInstance(this)
                .executeNetTask(baseTask);

    }

    public interface NetMsgListener{

        void onStart();


        void onSucceed(Object o);


        void onFailed(String msg);


        void onFinish();
    }

}
