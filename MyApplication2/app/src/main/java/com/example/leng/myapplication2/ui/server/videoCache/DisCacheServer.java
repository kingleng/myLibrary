package com.example.leng.myapplication2.ui.server.videoCache;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by leng on 2021/1/25.
 */
public class DisCacheServer implements BaseServer {

    BaseServer server;

    boolean hasCache;
    DiskCache diskCache;

    String mPath = getSDPath() + "/aegis22/videoDownload";

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    public DisCacheServer(BaseServer server) {
        this.server = server;
        diskCache = new DiskCache(mPath, 1024 * 1024 * 1024 * 30); //30G
    }

    @Override
    public HttpResponse getResponse(HttpRequest request) throws IOException {
//        hasCache = diskCache.getCacheHeaders(request.url,request);
        if(!hasCache){
            HttpResponse response = server.getResponse(request);

        }

        HttpResponse res = new HttpResponse();

        return res;
    }


}
