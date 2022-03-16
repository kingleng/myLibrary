package com.info.aegis.linphonelibrary.callback;

/**
 * Created by leng on 2020/6/17.
 */
public interface RegiserationState {

    void registrationNone();

    void registrationProgress();

    void registrationOk();

    void registrationCleared();

    void registrationFailed();
}
