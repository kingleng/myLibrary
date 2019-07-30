package com.kingleng.baselibrary.net.okhttp;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.kingleng.baselibrary.net.NetworkCallBack;
import com.kingleng.baselibrary.net.SSLSocketClient;

import static android.content.ContentValues.TAG;

/**
 * Created by leng on 2019/4/11.
 */
public class OKHttpHelper {

    private Map<String,String> params;
    private String url;
    private String method;
    private Request request;
    private Object mTag;
    private NetworkCallBack networkCallBack;

    public static class ReqMethod {
        public static String POST = "post";
        public static String GET = "get";
    }

    private static OKHttpHelper instance;

    public static OKHttpHelper getInstance(Context context){
        if(instance == null){
            synchronized (OKHttpHelper.class){
                if(instance == null){
                    instance = new OKHttpHelper(context);
                }
            }
        }
        return instance;
    }

    OkHttpClient okHttpClient;
    Context mContext;

    public OKHttpHelper(Context context) {
        this.mContext = context;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(10000, TimeUnit.MICROSECONDS);

        builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
        builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
//        X509TrustManager trustManager;
//        SSLSocketFactory sslSocketFactory;
//        try {
//            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[] { trustManager }, null);
//            sslSocketFactory = sslContext.getSocketFactory();
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        }
//        builder.sslSocketFactory(sslSocketFactory, trustManager);
//        builder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                Certificate[] localCertificates = new Certificate[0];
//                try {
//                    //获取证书链中的所有证书
//                    localCertificates = session.getPeerCertificates();
//                } catch (SSLPeerUnverifiedException e) {
//                    e.printStackTrace();
//                }
//                //打印所有证书内容
//                for (Certificate c : localCertificates) {
//                    Log.d(TAG, "verify: "+c.toString());
//                }
//                return true;
//            }
//        });
        okHttpClient = builder.build();

    }

    public OKHttpHelper addTag(Object mTag) {
        this.mTag = mTag;
        return this;
    }

    public OKHttpHelper addParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public OKHttpHelper addUrl(String url){
        this.url = url;
        Log.e("OKHttpHelper","url = "+url);
        return this;
    }

    public OKHttpHelper setMethod(String method) {
        this.method = method;
        return this;
    }

    public OKHttpHelper setNetworkCallBack(NetworkCallBack networkCallBack) {
        this.networkCallBack = networkCallBack;
        return this;
    }

    public void start(){
        if (method.equals(ReqMethod.POST)){

            FormBody.Builder builder = new FormBody.Builder();
            for(String key:params.keySet()){
                builder.add(key,params.get(key));
            }
            RequestBody requestBody = builder.build();

            request = new Request.Builder()
                    .url(url)
//                    .addHeader("Accept","application/json; charset=utf-8")
                    .post(requestBody)
                    .tag(mTag)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .method("GET",null)
                    .tag(mTag)
                    .build();
        }

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                networkCallBack.onFailure();
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonObject = (String)response.body().string();
                    try {
                        JSONObject jsonObject1 = new JSONObject(jsonObject);
                        networkCallBack.onSuccess(jsonObject1);
                    } catch (JSONException e) {
//                        e.printStackTrace();
                        networkCallBack.onFailure();
                    }

                }else{
                    networkCallBack.onFailure();
                }

            }
        });
    }

//    public void get(Activity mActivity, String mUrl, final NetworkCallBack networkCallBack){
//        //2.创建Request对象，设置一个url地址,设置请求方式。
//        Request request = new Request.Builder().url(mUrl).method("GET",null).tag(mActivity).build();
//        //3.创建一个call对象,参数就是Request请求对象
//        Call call = okHttpClient.newCall(request);
//        //4.请求加入调度，重写回调方法
//        call.enqueue(new Callback() {
//            //请求失败执行的方法
//            @Override
//            public void onFailure(Call call, IOException e) {
//                networkCallBack.onFailure();
//            }
//            //请求成功执行的方法
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response.isSuccessful()){
//                    String jsonObject = (String)response.body().string();
//                    try {
//                        JSONObject jsonObject1 = new JSONObject(jsonObject);
//                        networkCallBack.onSuccess(jsonObject1);
//                    } catch (JSONException e) {
////                        e.printStackTrace();
//                        networkCallBack.onFailure();
//                    }
//
//                }else{
//                    networkCallBack.onFailure();
//                }
//            }
//        });
//
//    }
//
//    public void post(Activity mActivity, String mUrl, Map<String, String> params, final NetworkCallBack networkCallBack){
//        //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
//        FormBody.Builder builder = new FormBody.Builder();
//        for(String key:params.keySet()){
//            builder.add(key,params.get(key));
//        }
//        RequestBody requestBody = builder.build();
//
//        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
//        Request request = new Request.Builder().url(mUrl).post(requestBody).tag(mActivity).build();
//        //4.创建一个call对象,参数就是Request请求对象
//        Call call = okHttpClient.newCall(request);
//        //5.请求加入调度,重写回调方法
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                networkCallBack.onFailure();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response.isSuccessful()){
//                    String jsonObject = (String)response.body().string();
//                    try {
//                        JSONObject jsonObject1 = new JSONObject(jsonObject);
//                        networkCallBack.onSuccess(jsonObject1);
//                    } catch (JSONException e) {
////                        e.printStackTrace();
//                        networkCallBack.onFailure();
//                    }
//
//                }else{
//                    networkCallBack.onFailure();
//                }
//            }
//        });
//
//    }

    /** 根据Tag取消请求 */
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

    /** 根据Tag取消请求 */
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

    /** 取消所有请求请求 */
    public void cancelAll() {
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /** 取消所有请求请求 */
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
            Log.d(TAG, "trustManagerForCertificates: "+certificate.toString());
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
            keyStore.load(in,password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
