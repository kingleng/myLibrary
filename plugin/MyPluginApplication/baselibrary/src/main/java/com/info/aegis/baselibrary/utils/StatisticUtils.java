package com.info.aegis.baselibrary.utils;

import android.content.Context;
import android.text.TextUtils;

import com.info.aegis.baselibrary.SharePreHelper;
import com.info.aegis.baselibrary.net.BaseTask;
import com.info.aegis.baselibrary.net.NetworkCallBack;
import com.info.aegis.baselibrary.net.OKHttpHelper;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by leng on 2019/9/9.
 */
public class StatisticUtils {

    public static boolean SWITCH = false;

    PagerStatistic pagerStatistic;
    ArticleStatistic articleStatistic;



    public StatisticUtils(Context mContext) {
        if(TextUtils.equals(SharePreHelper.getEnvironment(),"prd")){
            SWITCH = true;
        }else{
            SWITCH = false;
        }
        pagerStatistic = new PagerStatistic(mContext);
        articleStatistic = new ArticleStatistic(mContext);
    }

    public void reset(){
        pagerStatistic.reset();
        articleStatistic.reset();
    }

    public void setCode(String pageUrl, String page, String isArticle, String articleName, Long enterTime){
        pagerStatistic.setCode(pageUrl,page,isArticle,articleName,enterTime,"");
    }

    public void setArticleCode(String pageUrl, String page, String isArticle, String articleName, Long enterTime,String articleId){
        articleStatistic.setCode(pageUrl,page,isArticle,articleName,enterTime,articleId);
    }

    public void sendData(){
        pagerStatistic.sendData();
        articleStatistic.sendData();
    }

    public class PagerStatistic{

        Context mContext;

        private String sourceCode;
        private String source;
        private String deviceId;
        private String project;
        private String apkVersion;
        private String environment;
        private String page;
        private String pageUrl;
        private String articleId;
        private String isArticle;
        private String articleName;
        private Long enterTime;
        private Long stayTime;
        private String pageGuid;
        private String ip;
        private String cName;
        private String channelCode;
        private String pageCode;
        private String eventCode;

        public PagerStatistic(Context mContext) {
            this.mContext = mContext;
            this.sourceCode = SharePreHelper.getEventId();
            this.source = SharePreHelper.getEventName();
            this.deviceId = SharePreHelper.getDeviceId();
            this.apkVersion = SharePreHelper.getApkVersion();
            this.environment= SharePreHelper.getEnvironment();
            this.project = "PCN_AI";
//            pageGuid = "4efacf81-6317-48e1-aa12-a68cd8505fac";
            ip = "127.0.0.1";
            cName = "江苏";
            channelCode = "3";
            pageCode = "1";
            eventCode = "-1";
        }

        public void reset(){
            pageUrl = "";
            page = "";
            enterTime = 0L;
            stayTime = 0L;
        }

        public void setCode(String pageUrl, String page, String isArticle, String articleName, Long enterTime, String articleId){
            this.pageUrl = pageUrl;
            this.page = page;
            this.isArticle = isArticle;
            this.articleName = articleName;
            this.enterTime = enterTime;
            this.articleId = articleId;

            UUID uuid = UUID.randomUUID();
            pageGuid = uuid.toString();

        }

        public void sendData(){
            if(TextUtils.isEmpty(pageUrl)){
                return;
            }

            this.stayTime = System.currentTimeMillis()-enterTime;
            updataData();
        }

        private void updataData(){
            if(!SWITCH){
                return;
            }

            String url = MessageFormat.format("https://point.aegis-info.com/public/aegis/point/1.gif?project=PCN_AI" +
                            "&sourceCode={0}&source={1}&deviceId={2}&apkVersion={3}&environment={4}&pageUrl={5}&enterTime={6}&stayTime={7}" +
                            "&page={8}&isArticle={9}&articleName={10}&actionCode=-1" +
                            "&project={11}&articleId={12}&pageGuid={13}&ip={14}&cName={15}&channelCode={16}&pageCode={17}&eventCode={18}",
                    sourceCode,source,deviceId,apkVersion,environment,pageUrl,enterTime+"",stayTime+"",page,isArticle,articleName,
                    project,articleId,pageGuid,ip,cName,channelCode,pageCode,eventCode);

            HashMap<String,String> params = null;
            BaseTask baseTask = new BaseTask(url, OKHttpHelper.ReqMethod.GET, params, url, new NetworkCallBack() {
                @Override
                public void onSuccess(JSONObject jsonObject) {

                }

                @Override
                public void onFailure(String error) {

                }
            });

            OKHttpHelper.getInstance(mContext)
                    .executeNetTask(baseTask);

            reset();

        }


    }

    public class ArticleStatistic{

        Context mContext;

        private String sourceCode;
        private String source;
        private String deviceId;
        private String project;
        private String apkVersion;
        private String environment;
        private String page;
        private String pageUrl;
        private String articleId;
        private String isArticle;
        private String articleName;
        private Long enterTime;
        private Long stayTime;
        private String pageGuid;
        private String ip;
        private String cName;
        private String channelCode;
        private String pageCode;
        private String eventCode;

        public ArticleStatistic(Context mContext) {
            this.mContext = mContext;
            this.sourceCode = SharePreHelper.getEventId();
            this.source = SharePreHelper.getEventName();
            this.deviceId = SharePreHelper.getDeviceId();
            this.apkVersion = SharePreHelper.getApkVersion();
            this.environment= SharePreHelper.getEnvironment();
            this.project = "PCN_AI";
            ip = "127.0.0.1";
            cName = "江苏";
            channelCode = "3";
            pageCode = "1";
            eventCode = "-1";
        }

        public void reset(){
            pageUrl = "";
            page = "";
            enterTime = 0L;
            stayTime = 0L;
            articleId = "";
        }

        public void setCode(String pageUrl, String page, String isArticle, String articleName, Long enterTime, String articleId){
            this.pageUrl = pageUrl;
            this.page = page;
            this.isArticle = isArticle;
            this.articleName = articleName;
            this.enterTime = enterTime;
            this.articleId = articleId;
            UUID uuid = UUID.randomUUID();
//            pageGuid = "4efacf81-6317-48e1-aa12-a68cd8505fac";
            pageGuid = uuid.toString();

        }

        public void sendData(){
            if(TextUtils.isEmpty(pageUrl)){
                return;
            }

            this.stayTime = System.currentTimeMillis()-enterTime;
            updataData();
        }

        private void updataData(){
            if(!SWITCH){
                return;
            }

            String url = MessageFormat.format("https://point.aegis-info.com/public/aegis/point/1.gif?project=PCN_AI" +
                            "&sourceId={0}&source={1}&deviceId={2}&apkVersion={3}&environment={4}&pageUrl={5}&enterTime={6}&stayTime={7}" +
                            "&page={8}&isArticle={9}&articleName={10}&actionCode=-1" +
                            "&project={11}&articleId={12}&pageGuid={13}&ip={14}&cName={15}&channelCode={16}&pageCode={17}&eventCode={18}",
                    sourceCode,source,deviceId,apkVersion,environment,pageUrl,enterTime+"",stayTime+"",page,isArticle,articleName,
                    project,articleId,pageGuid,ip,cName,channelCode,pageCode,eventCode);

            HashMap<String,String> params = null;
            BaseTask baseTask = new BaseTask(url, OKHttpHelper.ReqMethod.GET, params, url, new NetworkCallBack() {
                @Override
                public void onSuccess(JSONObject jsonObject) {

                }

                @Override
                public void onFailure(String error) {

                }
            });

            OKHttpHelper.getInstance(mContext)
                    .executeNetTask(baseTask);

            reset();

        }


    }

}
