package com.example.leng.myapplication2.ui.server.videoCache;

import java.io.IOException;

/**
 * Created by leng on 2021/1/25.
 */
public interface BaseServer {

    public HttpResponse getResponse(HttpRequest request) throws IOException;

}
