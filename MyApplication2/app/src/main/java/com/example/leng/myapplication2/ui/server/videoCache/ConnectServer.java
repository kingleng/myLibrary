package com.example.leng.myapplication2.ui.server.videoCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by leng on 2021/1/25.
 */
public class ConnectServer implements BaseServer {

    @Override
    public HttpResponse getResponse(HttpRequest request) throws IOException {
        Socket socket = new Socket(request.realHost, request.realPort);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(request.getHeadText().getBytes(StandardCharsets.UTF_8));
        outputStream.flush();

        HttpResponse httpResponse = HttpResponse.parse(inputStream);

        httpResponse.inputStream = inputStream;
        httpResponse.socket = socket;

        return httpResponse;
    }


}
