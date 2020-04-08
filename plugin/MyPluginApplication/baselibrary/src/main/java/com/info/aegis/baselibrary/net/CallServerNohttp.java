//package com.info.aegis.baselibrary.net;
//
//import com.yanzhenjie.nohttp.NoHttp;
//import com.yanzhenjie.nohttp.download.DownloadListener;
//import com.yanzhenjie.nohttp.download.DownloadQueue;
//import com.yanzhenjie.nohttp.download.DownloadRequest;
//import com.yanzhenjie.nohttp.rest.OnResponseListener;
//import com.yanzhenjie.nohttp.rest.Request;
//import com.yanzhenjie.nohttp.rest.RequestQueue;
//
///**
// * 作者：jksfood on 2017/3/22 10:53
// */
//
//public class CallServerNohttp {
//
//    private static CallServerNohttp instance;
//
//    /**
//     * 请求队列。
//     */
//    private RequestQueue requestQueue;
//    private DownloadQueue downloadQueue;
//
//    private CallServerNohttp() {
//        requestQueue = NoHttp.newRequestQueue(3);
//        downloadQueue = NoHttp.newDownloadQueue(1);
//    }
//
//    /**
//     * 请求队列。
//     */
//    public synchronized static CallServerNohttp getInstance() {
//        if (instance == null)
//            synchronized (CallServerNohttp.class) {
//                if (instance == null)
//                    instance = new CallServerNohttp();
//            }
//        return instance;
//    }
//
//    /**
//     * 添加一个请求到请求队列。
//     *
//     * @param what      用来标志请求, 当多个请求使用同一个Listener时, 在回调方法中会返回这个what。
//     * @param request   请求对象。
//     * @param listener  结果回调对象。
//     */
//    public <T> void add(int what, Request<T> request, OnResponseListener listener) {
//        requestQueue.add(what, request, listener);
//    }
//
//    /**
//     * 取消这个sign标记的所有请求。
//     * @param sign 请求的取消标志。
//     */
//    public void cancelBySign(Object sign) {
//        requestQueue.cancelBySign(sign);
//    }
//
//    /**
//     * 取消队列中所有请求。
//     */
//    public void cancelAll() {
//        requestQueue.cancelAll();
//    }
//
//    /**
//     * 文件下载
//     * @param what 区分下载
//     * @param request
//     * @param listener
//     */
//    public void download(int what, DownloadRequest request, DownloadListener listener) {
//        downloadQueue.add(what, request, listener);
//    }
//}
