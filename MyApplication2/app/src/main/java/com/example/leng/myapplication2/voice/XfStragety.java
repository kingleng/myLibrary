package com.example.leng.myapplication2.voice;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by leng on 2020/8/20.
 * 科大讯飞 语音转文字
 */
public class XfStragety implements VoiceStragety {

    public static final String TAG = "XfStragety";
    Context mContext;

    public SpeechRecognizer mIat;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private String[] languageEntries;
    private String[] languageValues;
    private String language = "zh_cn";
    private int selectedNum = 0;

    private String resultType = "json";

    public boolean cyclic = false;//音频流识别是否循环调用

    private StringBuffer buffer = new StringBuffer();

    private static XfStragety instance = null;


    public static XfStragety getInstance(Context context){
        if(instance == null){
            instance = new XfStragety(context);
        }

        return instance;
    }

    public XfStragety(Context context) {
        this.mContext = context;
//        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
//        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID +"=5db11808");
    }

    @Override
    public void init() {

        //初始化识别无UI识别对象

        //使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);

        setParam();
    }

    VoiceCallback mVoiceCallback;

    @Override
    public void start(VoiceCallback voiceCallback) {
        if (mIat == null) {
            return;
        }
        this.mVoiceCallback = voiceCallback;
        //开始识别，并设置监听器
        mIat.startListening(mRecognizerListener);
    }

    @Override
    public void close() {
        if( null != mIat ){
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            toastCenter("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。

//            toastCenter(error.getPlainDescription(true));

            Log.d(TAG, error.getPlainDescription(true));

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            toastCenter("结束说话");

//            voice_bg.setVisibility(View.GONE);
            Log.d(TAG, "结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());

            printResult(results);

            if (isLast & cyclic) {
//                // TODO 最后的结果
//                Message message = Message.obtain();
//                message.what = 0x001;
//                han.sendMessageDelayed(message,100);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            toastCenter("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

//        toastCenter(resultBuffer.toString());
//        edit_text.setText(resultBuffer.toString());
        if(mVoiceCallback!=null){
            mVoiceCallback.result(resultBuffer.toString());
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {

        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
        mIat.setParameter(SpeechConstant.SUBJECT, null);
        //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //此处engineType为“cloud”
        String engineType = "cloud";
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, engineType);
        //设置语音输入语言，zh_cn为简体中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        //取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        //自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/aegis/msc/iat.wav");


    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
//                toastCenter("初始化失败，错误码：" + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                Toast.makeText(mContext, "初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}
