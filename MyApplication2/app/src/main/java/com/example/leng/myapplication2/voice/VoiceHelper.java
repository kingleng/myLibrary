package com.example.leng.myapplication2.voice;

import android.content.Context;

/**
 * Created by leng on 2020/8/7.
 */
public class VoiceHelper {

    private Context mContext;
    VoiceStragety stragety;

    public VoiceHelper(Context context) {
        this.mContext = context;
        stragety = XfStragety.getInstance(mContext);
    }

    public void setVoice() {
        stragety.init();
    }


    public void startVoice(VoiceCallback voiceCallback){
        stragety.start(voiceCallback);
    }

    public void close(){
        stragety.close();
    }

}
