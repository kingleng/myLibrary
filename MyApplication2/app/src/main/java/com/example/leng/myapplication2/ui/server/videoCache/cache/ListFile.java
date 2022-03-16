package com.example.leng.myapplication2.ui.server.videoCache.cache;

import java.io.File;

public interface ListFile {
    File consume();

    void server(File file);
}
