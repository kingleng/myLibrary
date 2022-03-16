package com.info.aegis.baselibrary.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by leng on 2019/4/11.
 */
public class OKHttpHelper {

    private Request request;
    private List<BaseTask> tasks;
    Handler mHandler;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //do something
                if (tasks.size() > 0) {
                    BaseTask task = tasks.remove(0);
                    start(task);
                }
            }
            super.handleMessage(msg);

        }
    };

    class MyThread extends Thread {//这里也可用Runnable接口实现

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);//每隔1s执行一次
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static class ReqMethod {
        public static String POST = "post";
        public static String GET = "get";
    }

    private static OKHttpHelper instance;

    public static OKHttpHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (OKHttpHelper.class) {
                if (instance == null) {
                    instance = new OKHttpHelper(context);
                }
            }
        }
        return instance;
    }

    OkHttpClient okHttpClient;
    Context mContext;

    //cookie存储
    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    public OKHttpHelper(Context context) {
        this.mContext = context;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);//设置连接超时时间
        builder.readTimeout(20, TimeUnit.SECONDS);//设置读取超时时间

        builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
        builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        okHttpClient = builder
                .cookieJar(new CookieJar() {//这里可以做cookie传递，保存等操作
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {//可以做保存cookies操作
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {//加载新的cookies
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();

        /**
         * 获取主线程的handler
         */
        mHandler = new Handler(Looper.getMainLooper());
        tasks = new ArrayList<>();

        new Thread(new MyThread()).start();
    }

    public void executeNetTask(BaseTask baseTask) {
        tasks.add(baseTask);
    }

    public void start(final BaseTask baseTask) {

        if (baseTask.method.equals(ReqMethod.POST)) {

            RequestBody requestBody;

            if (!baseTask.isJson) {
                FormBody.Builder builder = new FormBody.Builder();
                if (baseTask.params != null) {
                    for (String key : baseTask.params.keySet()) {
                        builder.add(key, baseTask.params.get(key));
                    }
                }
                requestBody = builder.build();
            } else {
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), baseTask.json);
            }

            request = new Request.Builder()
                    .url(baseTask.url)
                    .post(requestBody)
                    .tag(baseTask.mTag)
                    .build();
        } else {
            String requestUrl;
            if (baseTask.params != null) {
                StringBuilder tempParams = new StringBuilder();
                int pos = 0;
                for (String key : baseTask.params.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    tempParams.append(String.format("%s=%s", key, baseTask.params.get(key)));
                    pos++;
                }
                requestUrl = String.format("%s?%s", baseTask.url, tempParams.toString());
            } else {
                requestUrl = baseTask.url;
            }

            request = new Request.Builder()
                    .url(requestUrl)
                    .addHeader("Accept", "application/json; charset=utf-8")
                    .method("GET", null)
                    .tag(baseTask.mTag)
                    .build();


        }

        Log.e("okhttp::", "mUrl = " + baseTask.url);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (e instanceof SocketTimeoutException) {//判断超时异常
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            baseTask.networkCallBack.onFailure("请求超时");
                        }
                    });
                    return;
                }
                if (e instanceof ConnectException) {//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            baseTask.networkCallBack.onFailure("服务器连接异常");
                        }
                    });
                    return;
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        baseTask.networkCallBack.onFailure("接口数据异常");
                    }
                });

            }

            @Override
            public void onResponse(final Call call, Response response) {

                if (response.isSuccessful()) {
                    try {
                        String jsonObject = response.body().string();
                        final JSONObject jsonObject1 = new JSONObject(jsonObject);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                baseTask.networkCallBack.onSuccess(jsonObject1);
                            }
                        });
                    } catch (Exception e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                baseTask.networkCallBack.onFailure("请求成功，接口数据异常");
                            }
                        });
                    }
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            baseTask.networkCallBack.onFailure("请求成功，接口数据异常");
                        }
                    });
                }
            }
        });

    }

    /**
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        tasks.clear();
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 取消所有请求请求
     */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }


    private InputStream trustedCertificatesInputStream() {
        InputStream inputStream = null;
        try {
            inputStream = mContext.getAssets().open("yghq.cer");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    private X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        char[] password = "password".toCharArray();
        // Put the certificates a key store.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            Log.d(TAG, "trustManagerForCertificates: " + certificate.toString());
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }
        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null;
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
