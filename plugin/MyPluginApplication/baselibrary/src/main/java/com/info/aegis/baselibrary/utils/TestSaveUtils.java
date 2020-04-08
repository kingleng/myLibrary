package com.info.aegis.baselibrary.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Reader;

public class TestSaveUtils {

    private static String mPath = getSDPath() + "/aegis/info";
    private static String mfile = "information.out";

    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    //保存在sd卡
    public static boolean fileSaveSDCard(String obj) {

        File pFile = new File(mPath);
        if (!pFile.exists()) {
            pFile.mkdirs();
        }

        File sdFile = new File(mPath,"information.out");

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(sdFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj); //写入
            oos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
                if (oos != null) oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 将String数据存为文件
     */
    public static File wtiteFileFromBytes(String name) {
        byte[] b=name.getBytes();
        BufferedOutputStream stream = null;
        File file = null;
        try {
            File pFile = new File(mPath);
            if (!pFile.exists()) {
                pFile.mkdirs();
            }

            file = new File(mPath,mfile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static String getFileData(){
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(mPath+"/"+mfile);
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");//文件编码Unicode,UTF-8,ASCII,GB2312,Big5
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char)ch);
            }
            in.close();
        } catch (IOException e) {
            Log.e("ddd","文件不存在!");
            return "";
        }
        Log.e("ddd",buffer.toString());
        return buffer.toString();  //buffer.toString())就是读出的内容字符
    }

}