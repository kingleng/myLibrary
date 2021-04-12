package com.example.leng.myapplication2.ui.server.videoCache.utils;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}