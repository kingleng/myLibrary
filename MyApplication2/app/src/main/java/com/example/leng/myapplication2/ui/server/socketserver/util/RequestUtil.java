package com.example.leng.myapplication2.ui.server.socketserver.util;


import com.example.leng.myapplication2.ui.server.socketserver.HttpRequest;
import com.example.leng.myapplication2.ui.server.socketserver.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class RequestUtil {
    public static HttpResponse getHttpResponseFromNet(HttpRequest request) {
        Socket socket;
        HttpResponse response = null;
        HttpRequest re = request;
        boolean redirect;
        do {
            try {
                int port = 80;
                try {
                    port = Integer.parseInt(re.getHeaders().get(Constant.HOST_PORT));
                } catch (Exception ignored) {
                }

                if(port == 443){
                    // 自定义的管理器
                    X509TrustManager xtm = new Java2000TrustManager();
                    TrustManager mytm[] = { xtm };
                    // 得到上下文
                    SSLContext ctx = SSLContext.getInstance("SSL");
                    // 初始化
                    ctx.init(null, mytm, null);
                    // 获得工厂
                    SSLSocketFactory factory = ctx.getSocketFactory();
                    // 从工厂获得Socket连接
                    socket = factory.createSocket(re.getHost(), port);

//                    socket = (SSLSocket)((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(re.getHost(), port);
                }else{
                    socket = new Socket(re.getHost(), port);
                }

                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(re.getHeadText().getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                response = HttpResponse.parse(inputStream);
                response.setSocket(socket);

                redirect = response.getStatusCode() == 301 || response.getStatusCode() == 302 || response.getStatusCode() == 303;

                if (redirect) {
                    String location = response.getHeaders().get(Constant.LOCATION);
                    if (!StringUtil.isEmpty(location)) {
                        URL url = new URL(location);
                        re = new HttpRequest();
                        re.getHeaders().putAll(request.getHeaders());
                        re.setMethod(Constant.METHOD_GET);
                        re.setProtocol(Constant.HTTP_VERSION_1_1);
                        re.setUrl(url.getPath()+"?"+url.getQuery());
                        re.setHost(url.getHost());
                        int redirectPort = url.getPort();
                        if (redirectPort == -1 || redirectPort == 80) {
                            redirectPort = 443;
                        }
                        re.getHeaders().put(Constant.HOST_PORT, String.valueOf(redirectPort));
                    }
                    socket.close();
                }
            } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
                break;
            }

        } while (redirect);

        return response;
    }

    public static String getRealHostNameWithPort(HttpRequest request) {
        String url = request.getUrl();
        Matcher matcher = Pattern.compile(Constant.REAL_HOST_NAME + "=([^&]*)").matcher(url);

        if (matcher.find()) {
            String realHostName = matcher.group(1);
            int realHostPort = 80;
            if (realHostName.contains(":")) {
                String[] split = realHostName.split(":");
                realHostName = split[0];
                realHostPort = Integer.parseInt(split[1]);
            }
            return realHostName + ":" + realHostPort;
        }
        return null;
    }


}
