package com.info.aegis.baselibrary.utils.download;


import android.text.TextUtils;

import io.reactivex.functions.Consumer;
import rxhttp.wrapper.exception.HttpStatusCodeException;
import rxhttp.wrapper.exception.ParseException;


/**
 * RxJava 错误回调 ,加入网络异常处理
 * User: ljx
 * Date: 2019/04/29
 * Time: 11:15
 */
public interface OnError extends Consumer<Throwable> {

    @Override
    default void accept(Throwable throwable) throws Exception {


    }

    //返回事件是否被消费
    boolean onError(Throwable throwable, String errorMsg) throws Exception;
}
