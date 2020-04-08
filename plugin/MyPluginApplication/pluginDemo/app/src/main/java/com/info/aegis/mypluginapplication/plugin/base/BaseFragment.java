package com.info.aegis.mypluginapplication.plugin.base;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.info.aegis.baselibrary.base.LibraryFragment;
import com.info.aegis.baselibrary.utils.NetworkUtils;

/**
 * Created by j on 2019/4/1.
 */
public class BaseFragment extends LibraryFragment {

    public static final String TAG = "BaseFragment";


    public boolean isNetWorkAvailable(Context context) {
        return NetworkUtils.isNetWorkAvailable(context);
    }

    public void getResultSuccess(Object object, String type) {

    }

    public void getResultFailed(String type) {

    }

    //隐藏软键盘
    public void hide() {
        if (getActivity() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }


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

    public void setVovice() {
        //初始化识别无UI识别对象
        //使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(getContext(), mInitListener);

        setParam();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {

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
                toastCenter("初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };


    Toast toast;

    public void toastCenter(String msg) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
