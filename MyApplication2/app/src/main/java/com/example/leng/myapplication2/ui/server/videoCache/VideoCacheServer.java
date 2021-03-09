package com.example.leng.myapplication2.ui.server.videoCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by leng on 2021/1/18.
 */
public class VideoCacheServer {

    public static final String REAL_HOST_NAME = "realhost";
    public static final String HOST_PORT = "RealHostPort";
    public static final String PROXY_HOST = "ProxyHost";
    public static final String HOST = "Host";
    public static final String IF_RANGE = "If-Range";
    public static final String CONNECTION = "Connection";

    String mCacheFile = "";
    int curPort = 9090;

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    ExecutorService pool = Executors.newFixedThreadPool(20);

    boolean isRunning = true;

    public VideoCacheServer(String cacheFile) {
        mCacheFile = cacheFile;
    }

    public void start(){

        try {
            final ServerSocket serverSocket = new ServerSocket(curPort);
            isRunning = true;
            singleThreadExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    while (isRunning){
                        try {
                            Socket connection = serverSocket.accept();
                            connection.setKeepAlive(true);
                            pool.submit(new HandlerProxy(connection));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            start();
        }

    }

    public void destroy(){
        isRunning = false;
    }

    public String getProxyUrl(String url){
        String encodeUrl = url;
        try {
            String proxyAddr = "127.0.0.1";
            URL u = new URL(encodeUrl);
            String realHost = u.getHost();
            int realPort = u.getPort();
            if (realPort == -1) {
                realPort = 80;
            }
            String proxyHostPort = proxyAddr + ":" + curPort;
            encodeUrl = encodeUrl.replace("https", "http");

            if (encodeUrl.contains(realHost + ":" + realPort)) {
                encodeUrl = encodeUrl.replace(realHost + ":" + realPort, proxyHostPort);
            } else {
                encodeUrl = encodeUrl.replace(realHost, proxyHostPort);
            }
            return encodeUrl + ((encodeUrl.contains("?") ? "&" : "?") + REAL_HOST_NAME + "=" + realHost + ":" + realPort);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    class HandlerProxy implements Runnable {

        Socket comment;

        public HandlerProxy(Socket comment) {
            this.comment = comment;
        }

        @Override
        public void run() {
            try {
                //获取请求代理服务器的socket；
                BufferedInputStream inputStream = new BufferedInputStream(comment.getInputStream());
                BufferedOutputStream outputStream = new BufferedOutputStream(comment.getOutputStream());
                //处理获取真实的socket请求信息
                HttpRequest httpRequest = HttpRequest.parse(inputStream);
                //用真实的请求信息进行网络请求并获取服务器的返回数据流
                HttpResponse httpResqonse = new ConnectServer().getResponse(httpRequest);
                //把服务器返回的数据流写到代理服务器中，通过代理服务器发给请求对象
                writeStream(httpResqonse,outputStream);


            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(comment!=null){
                    try {
                        comment.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void writeStream(HttpResponse httpResqonse, OutputStream outputStream) throws IOException {

            outputStream.write(httpResqonse.getHeadText().getBytes(Charset.forName("UTF-8")));

            InputStream remote_in = httpResqonse.inputStream;
            if(remote_in!=null){
                BufferedInputStream bufferedInputStream = new BufferedInputStream(remote_in);
                byte[] bytes = new byte[1024*512];
                int length = 0;
                while ((length=bufferedInputStream.read(bytes,0,bytes.length)) != -1){
                    outputStream.write(bytes,0,length);
                }
                outputStream.flush();
            }else{
                outputStream.write("\r\n".getBytes(Charset.forName("UTF-8")));
            }

            if(outputStream!=null){
                outputStream.close();
            }
            if(httpResqonse.inputStream!=null){
                httpResqonse.inputStream.close();
            }
            if(httpResqonse.socket!=null){
                httpResqonse.socket.close();
            }

        }
    }

}
