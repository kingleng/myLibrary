package com.info.aegis.baselibrary.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.info.aegis.baselibrary.net.BaseTask;
import com.info.aegis.baselibrary.net.NetworkCallBack;
import com.info.aegis.baselibrary.net.OKHttpHelper;
import com.info.aegis.baselibrary.utils.StatisticUtils;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by leng on 2019/9/9.
 */
public class LibraryFragment extends Fragment {

    public StatisticUtils statisticUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statisticUtils = new StatisticUtils(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        statisticUtils.reset();
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
    public void onStop() {
        super.onStop();
        statisticUtils.sendData();
    }

    public void requstInfo(String url, NetMsgListener listener) {
        requstInfo(url, null, listener);
    }

    public void requstPostInfo(String url, NetMsgListener listener) {
        requstInfo(url, null, "post", listener);
    }

    public void requstInfo(String url, Map<String, String> params, NetMsgListener listener) {
        requstInfo(url, params, "get", listener);
    }

    public void requstInfo(String url, Map<String, String> params, String method, NetMsgListener listener) {

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

        OKHttpHelper.getInstance(getActivity())
                .executeNetTask(baseTask);

    }


    public interface NetMsgListener {

        void onStart();

        void onSucceed(Object o);

        void onFailed(String msg);

        void onFinish();
    }
}
