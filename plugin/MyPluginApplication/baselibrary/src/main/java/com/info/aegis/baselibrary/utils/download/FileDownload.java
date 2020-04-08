package com.info.aegis.baselibrary.utils.download;

import android.os.Environment;
import android.util.Log;

import com.info.aegis.baselibrary.event.DownloadEvent;
import com.info.aegis.baselibrary.utils.MD5;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import rxhttp.wrapper.param.RxHttp;

/**
 * Created by leng on 2019/6/25.
 */
public class FileDownload {

    public static final int MAX_TASK_COUNT = 3;  //最大并发数

    private List<DownloadInfo> waitTask = new ArrayList<>(); //等待下载的任务
    private List<DownloadInfo> downloadingTask = new ArrayList<>(); //等待下载的任务

    private String mPath = "/aegis/videoDownload";

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


    public FileDownload() {
    }

    private static FileDownload instance;

    public static FileDownload getInstance(){
        if(instance == null){
            instance = new FileDownload();
        }
        return instance;
    }

    public void download(DownloadInfo data, FileDownloadCallBack callBack) throws Exception {
        if(callBack==null){
            return;
        }

        if (downloadingTask.size() >= MAX_TASK_COUNT) {
//            data.setState(1);
            waitTask.add(data);
            return;
        }

        String destPath = getSDPath() + mPath + "/" + MD5.md5(data.getUrl()) + ".mp4.download";
        String cachePath = getSDPath() + mPath + "/" + MD5.md5(data.getUrl()) + ".mp4";

        File destFile = new File(destPath);
        File cacheFile = new File(cachePath);
        long length = new File(destPath).length();
        Log.e("ccc","destPath:"+destPath +"  :::  length:"+length);
        Disposable disposable = RxHttp.get(data.getUrl())
                .setRangeHeader(length)  //设置开始下载位置，结束位置默认为文件末尾
                .asDownload(destPath, length, stringProgress -> {
                    if (!stringProgress.isCompleted()) {
                        data.setProgress(stringProgress.getProgress());//当前进度 0-100
                        data.setCurrentSize(stringProgress.getCurrentSize());//当前已下载的字节大小
                        data.setTotalSize(stringProgress.getTotalSize()); //要下载的总字节大小
                        EventBus.getDefault().post(new DownloadEvent(data));
                    }
                })  //如果需要衔接上次的下载进度，则需要传入上次已下载的字节数
                .observeOn(AndroidSchedulers.mainThread()) //主线程回调
//                .filter(Progress::isCompleted)//过滤事件，下载完成，才继续往下走
//                .map(Progress::getResult) //到这，说明下载完成，拿到Http返回结果并继续往下走
//                .doFinally(() -> {//不管任务成功还是失败，如果还有在等待的任务，都开启下一个任务
//                    downloadingTask.remove(data);
//                    if (waitTask.size() > 0)
//                        download(waitTask.remove(0),callBack);
//                })
//                .as(RxLife.as(this)) //加入感知生命周期的观察者
                .subscribe(s -> { //s为String类型
                    destFile.renameTo(cacheFile);
                    callBack.onSuccess(data);
//                    Tip.show("下载完成" + s);
//                    data.setState(4);
//                    notifyDataSetChanged(true);
                    //下载成功，处理相关逻辑
                }, (OnError) (throwable, errorMsg) -> {
//                    data.setState(5);
                    callBack.onFailure();
                    return true;
                    //下载失败，处理相关逻辑
                });
//        data.setState(2);
        downloadingTask.add(data);
        data.setDisposable(disposable);
    }
}
