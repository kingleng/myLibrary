package com.info.aegis.baselibrary.utils;

import android.content.Context;

import com.info.aegis.baselibrary.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 敏感词过滤屏蔽
 * @see #parse 直接调用此方法后返回字符串 星号*替换原敏感词
 * NOTE:
 * 1.调用init初始化敏感词库
 * 2.手动添加敏感词追加到R.raw.wd.txt的时候注意编码转换成UTF-8
 * @author gallon on 2018/1/15.
 */
public class TextFilter {

    private static List<String> list = new ArrayList<>();

    public static void init(Context context) {
//        try {
//            InputStream is = context.getResources().openRawResource(R.raw.wd);
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader bf = new BufferedReader(isr);
//            String line = "";
//            while (line != null) {
//                line = bf.readLine();
//                if (line != null && !line.equals("")) {
//                    list.add(line);
//                }
//            }
//        } catch (FileNotFoundException e) {
//            CustomToast.showLongToast("文件没找到");
//            e.printStackTrace();
//        } catch (IOException e) {
//            CustomToast.showLongToast("IO异常");
//            e.printStackTrace();
//        }
        CustomToast.showLongToast("文件没找到");
    }

    private static String[] replacement = new String[]{"", "*", "**", "***", "****", "*****", "******", "*******", "********", "*********", "**********"};

    /**
     * 屏蔽敏感词 星号*替换原敏感词
     * @param content
     * @return
     */
    public static String parse(String content) {
        for (String s : list) {
            int length = s.length();
            if (length >= replacement.length) continue;
            content = content.replace(s.toUpperCase(), replacement[length]);
            content = content.replace(s.toLowerCase(), replacement[length]);
        }
        return content;
    }

}
