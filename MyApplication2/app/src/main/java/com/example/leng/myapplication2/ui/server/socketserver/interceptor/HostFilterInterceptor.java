package com.example.leng.myapplication2.ui.server.socketserver.interceptor;


import com.example.leng.myapplication2.ui.server.socketserver.HttpRequest;
import com.example.leng.myapplication2.ui.server.socketserver.HttpResponse;
import com.example.leng.myapplication2.ui.server.socketserver.util.Constant;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HostFilterInterceptor implements Interceptor {
    private final static Logger logger = Logger.getLogger("CacheInterceptor");
    private int proxyServerPort;

    public HostFilterInterceptor(int port) {
        proxyServerPort = port;
    }

    @Override
    public HttpResponse intercept(Chain chain) {
        HttpRequest request = chain.getRequest();
        String url = request.getUrl();
        Matcher matcher = Pattern.compile(Constant.REAL_HOST_NAME + "=([^&]*)").matcher(url);

        if (!matcher.find() || matcher.groupCount() < 1) {
            return HttpResponse.get404Response();
        } else {
            String realHostName = matcher.group(1);
            int realHostPort = 80;
            if (realHostName.contains(":")) {
                String[] split = realHostName.split(":");
                realHostName = split[0];
                realHostPort = Integer.parseInt(split[1]);
            }


            String proxyHost = request.getHost();
            if (proxyHost != null) {
                proxyHost = proxyHost.trim();
            }
            request.getHeaders().put(Constant.PROXY_HOST, proxyHost);
            request.getHeaders().put(Constant.HOST, realHostName);
            request.getHeaders().put(Constant.HOST_PORT, String.valueOf(realHostPort));
            request.getHeaders().remove(Constant.IF_RANGE);
            request.getHeaders().put(Constant.CONNECTION, "close");
            request.getHeaders().put(Constant.IS_HTTPS, url.contains("&https=true")+"");

            if (request.getHeaders().get(Constant.REFERER) != null) {
                String referer = String.format("http://%s:%s%s", proxyHost, proxyServerPort, url);
                request.getHeaders().put(Constant.REFERER, referer);
            }

            if (Constant.enableLog) {
                logger.log(Level.INFO, "after host filter \n");
                logger.log(Level.INFO, request.getHeadText());
            }
            return chain.proceed(request);
        }
    }

}
