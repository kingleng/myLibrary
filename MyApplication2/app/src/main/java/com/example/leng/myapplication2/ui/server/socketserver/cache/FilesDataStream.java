package com.example.leng.myapplication2.ui.server.socketserver.cache;

import com.example.leng.myapplication2.ui.server.socketserver.util.CloseUtil;
import com.example.leng.myapplication2.ui.server.socketserver.util.Constant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilesDataStream extends InputStream {
    private static final Logger logger = Logger.getLogger("FilesDataStream");

    private ListFile files;
    private int totalLength;
    private int pos;

    private InputStream bis;

    private int startOffset;

    private boolean curIsFirstFile = true;

    public FilesDataStream(ListFile files, int totalLength) {
        this(files, 0, totalLength);
    }

    public FilesDataStream(ListFile files, int startOffset, int totalLength) {
        this.totalLength = totalLength;
        pos = 0;
        this.startOffset = startOffset;
        this.files = files;
    }


    @Override
    public int read() throws IOException {
        if (pos >= totalLength) {
            return -1;
        }
        if (curIsFirstFile) {
            File f = files.consume();
            if (f == null || !f.exists()) {
                return -1;
            }
            bis = new BufferedInputStream(new FileInputStream(f));
            bis.skip(startOffset);
            curIsFirstFile = false;
        }
        if (bis == null) {
            return -1;
        }
        int read = -1;
        try {
            read = bis.read();
            if (read == -1) {
                CloseUtil.close(bis);
                File consumeFile = files.consume();
                if (consumeFile == null || !consumeFile.exists()){
                    bis = null;
                    return -1;
                }
                bis = new BufferedInputStream(new FileInputStream(consumeFile));
                read = bis.read();
            }
        } catch (Exception e) {
            if(Constant.enableLog){
                logger.log(Level.SEVERE, "read exception", e);
            }
            CloseUtil.close(bis);
            bis = null;

        }

        pos++;
        return read;
    }

    @Override
    public void close() throws IOException {
        super.close();
        CloseUtil.close(bis);
    }

    public int getTotalLength() {
        return totalLength;
    }
}
