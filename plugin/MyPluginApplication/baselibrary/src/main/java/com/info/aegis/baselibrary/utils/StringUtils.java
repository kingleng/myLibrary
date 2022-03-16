package com.info.aegis.baselibrary.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.info.aegis.baselibrary.SharePreHelper;

/**
 * Created by SFS on 2017/8/7.
 * Description :
 */

public class StringUtils {

    /**
     * 修改Json 数据格式
     * @param s
     * @return
     */
    public static String parse(String s) {

        StringBuilder sb = new StringBuilder();

        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (i > 0 && chars[i] == '{' && chars[i - 1] == '"') {
                sb.setLength(sb.length() - 1);
                sb.append(chars[i]);
                continue;
            }
            if (i > 0 && chars[i - 1] == '}' && chars[i] == '"') {
                continue;
            }
            if (i > 0 && chars[i - 1] == '\\' && chars[i] == '"') {
                sb.setLength(sb.length() - 1);
                sb.append(chars[i]);
                continue;
            }
            if (i > 0 && chars[i - 1] == '\\' && chars[i] == '\\') {
                continue;
            }
            sb.append(chars[i]);
        }
        return sb.toString();
    }


    /**
     * String 直接替换
     * @param content
     * @return
     */
    public static String parseContent(String content) {


        content = content.replace("<b>", "<font color='#FF1C3E'>");
        content = content.replace("</b>", "</font>");
        content = content.replace("<\n>", "<br>");


        return content;
    }
//    /**
//     * StringBuilder替换标签 增加 关键字高亮/包括换行符
//     * @param content
//     * @return
//     */
//    @NonNull
//    public static String parseTag(String content) {
//        content=content.replace("\n","<br />"); //  处理 字符串中的 \n
//        StringBuilder sb = new StringBuilder();
//
//
//        char[] chars = content.toCharArray();
//
//
//        for (int i = 0; i < chars.length; i++) {
//            sb.append(chars[i]);
//            if (i > 0 && chars[i] == 'b' && chars[i - 1] == '<' && chars[i + 1] == '>') {
//                sb.setLength(sb.length()-1);
//                sb.append("font color='#FF1C3E'");
//                continue;
//            }
//
//            if (i > 0 && chars[i] == 'b' && chars[i - 1] == '/' && chars[i + 1] == '>') {
//                sb.setLength(sb.length()-1);
//                sb.append("font");
//                continue;
//            }
//
//        }
//
//        return sb.toString();
//    }

    /**
     * StringBuilder 替换字符串
     * @param content
     * @return
     */
    public static String parseTag(String content) {
        content = content.replace("\n", "<br />");
        StringBuilder sb = new StringBuilder();
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[i]);
            if (i > 0 && chars[i] == 'b' && chars[i - 1] == '<' && chars[i + 1] == '>') {
                sb.setLength(sb.length() - 1);
                sb.append("font color='#c43725'");
                continue;
            }
            if (i > 0 && chars[i] == 'b' && chars[i - 1] == '/' && chars[i + 1] == '>') {
                sb.setLength(sb.length() - 1);
                sb.append("font");
                continue;
            }
        }
        return sb.toString();
    }
    /**
     * StringBuilder 替换字符串
     * @param content
     * @return
     */
    public static String parseTag3(String content) {
        content = content.replace("br /", "\n");
        StringBuilder sb = new StringBuilder();
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[i]);
            if (i > 0 && chars[i] == 'b' && chars[i - 1] == '<' && chars[i + 1] == '>') {
                sb.setLength(sb.length() - 1);
                sb.append("font color='#FF1C3E'");
                continue;
            }
            if (i > 0 && chars[i] == 'b' && chars[i - 1] == '/' && chars[i + 1] == '>') {
                sb.setLength(sb.length() - 1);
                sb.append("font");
                continue;
            }
        }
        return sb.toString();
    }


    /**
     * StringBuilder 移除变色字符串
     * @param content
     * @return
     */
    public static String parseTag2(String content) {
        StringBuilder sb = new StringBuilder();

        char[] chars = content.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[i]);
            if (i > 1 && chars[i] == '>' && chars[i - 1] == 'b' && chars[i - 2] == '<') {
                sb.setLength(sb.length() - 3);
                continue;
            }

            if (i > 2 && chars[i] == '>' && chars[i - 1] == 'b' && chars[i - 2] == '/' && chars[i - 3] == '<') {
                sb.setLength(sb.length() - 4);
                continue;
            }
        }
        return sb.toString();
    }

    /**
     * 去除多余的前后缀
     * @param content
     */
    public static String cutString(String content) {
        content = content.trim();
        char c = content.charAt(0);
        while (c == '，' || c == ',' || c == '?' || c == '？' || c == '!' || c == '！' || c == '.' || c == '。') {
            content = content.substring(1);
            c = content.charAt(0);
        }
        c = content.charAt(content.length() - 1);
        while (c == '，' || c == ',' || c == '?' || c == '？' || c == '!' || c == '！' || c == '.' || c == '。') {
            content = content.substring(0, content.length() - 1);
            c = content.charAt(content.length() - 1);
        }
        return content;
    }





}
