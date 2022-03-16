package com.example.leng.myapplication2.ui.server.socketserver.interceptor;


import com.example.leng.myapplication2.ui.server.socketserver.HttpRequest;
import com.example.leng.myapplication2.ui.server.socketserver.HttpResponse;
import com.example.leng.myapplication2.ui.server.socketserver.exception.RequestException;
import com.example.leng.myapplication2.ui.server.socketserver.util.RequestUtil;

public class ConnectInterceptor implements Interceptor {

    @Override
    public HttpResponse intercept(Chain chain) throws RequestException {
        HttpRequest request = chain.getRequest();
        HttpResponse response = RequestUtil.getHttpResponseFromNet(request);

        if (!response.isOK()) {
            throw new RequestException("request not ok :" + response.getHeadText());
        }
        return response;
    }

}
