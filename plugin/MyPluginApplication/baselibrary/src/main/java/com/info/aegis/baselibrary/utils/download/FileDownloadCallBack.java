package com.info.aegis.baselibrary.utils.download;

/**
 * Created by leng on 2019/4/11.
 */
public interface FileDownloadCallBack {

    void onSuccess(DownloadInfo data);

    void onFailure();

}
