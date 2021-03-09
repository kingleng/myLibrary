package com.example.leng.myapplication2.ui.server.socketserver.interceptor;


import com.example.leng.myapplication2.ui.server.socketserver.HttpRequest;
import com.example.leng.myapplication2.ui.server.socketserver.HttpResponse;
import com.example.leng.myapplication2.ui.server.socketserver.exception.RequestException;

public interface Interceptor {
    HttpResponse intercept(Chain chain) throws RequestException;

    interface Chain {
        HttpRequest getRequest();

        HttpResponse proceed(HttpRequest request);
    }
}
