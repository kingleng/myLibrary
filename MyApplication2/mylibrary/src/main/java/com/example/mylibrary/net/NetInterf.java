package com.example.mylibrary.net;

/**
 * Created by leng on 2017/5/2.
 */

public interface NetInterf {
    void uiSuccess(NetMessageInfo msg);
    void uiFailure(NetMessageInfo msg);
    void uiError(NetMessageInfo msg);
    void uiFinish(NetMessageInfo msg);
}
