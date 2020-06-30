package com.info.aegis.linphonelibrary.callback;

import org.linphone.core.Call;

/**
 * Created by leng on 2020/6/17.
 */
public interface CallState {
    void incomingCall(Call call);

    void connected();

    void end();

    void outgoingInit();
}
