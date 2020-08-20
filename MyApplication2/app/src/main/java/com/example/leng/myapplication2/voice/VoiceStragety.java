package com.example.leng.myapplication2.voice;

/**
 * Created by leng on 2020/8/20.
 */
public interface VoiceStragety {
    void init();

    void start(VoiceCallback voiceCallback);

    void close();
}
