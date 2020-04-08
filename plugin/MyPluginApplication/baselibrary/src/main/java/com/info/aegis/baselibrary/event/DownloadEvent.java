package com.info.aegis.baselibrary.event;

import com.info.aegis.baselibrary.utils.download.DownloadInfo;

/**
 * Created by leng on 2019/5/20.
 */
public class DownloadEvent {

    public DownloadInfo downloadInfo;

    public DownloadEvent(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

}
