//package com.example.mylibrary.image;
//
//
//import android.content.Context;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.GlideBuilder;
//import com.bumptech.glide.load.model.GlideUrl;
//import com.bumptech.glide.module.GlideModule;
//
//import java.io.InputStream;
//import java.security.SecureRandom;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import okhttp3.OkHttpClient;
//
//public class OkHttpGlideModule implements GlideModule {
//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        // Do nothing.
//    }
//
//    @Override
//    public void registerComponents(Context context, Glide glide) {
//        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
//        mBuilder.sslSocketFactory(createSSLSocketFactory());
//        mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
//        OkHttpClient client = mBuilder.build();
////        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
//        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
//    }
//
//    private static class TrustAllCerts implements X509TrustManager {
//        @Override
//        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
//    }
//
//    private static class TrustAllHostnameVerifier implements HostnameVerifier {
//        @Override
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    }
//
//    private static SSLSocketFactory createSSLSocketFactory() {
//        SSLSocketFactory ssfFactory = null;
//
//        try {
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());
//
//            ssfFactory = sc.getSocketFactory();
//        } catch (Exception e) {
//        }
//
//        return ssfFactory;
//    }
//
//}