package com.example.leng.myapplication.view.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leng on 2016/12/16.
 */

public class FaceUtil {
    private static Map<String, String> faceMap = new HashMap<String, String>();

    static {
        faceMap.put("[微笑]", "expression_1");
        faceMap.put("[撇嘴]", "expression_2");
        faceMap.put("[色]", "expression_3");
        faceMap.put("[发呆]", "expression_4");
        faceMap.put("[得意]", "expression_5");
        faceMap.put("[流泪]", "expression_6");
        faceMap.put("[害羞]", "expression_7");
        faceMap.put("[闭嘴]", "expression_8");
    }

    public static SpannableString getExpressionString(Context context, String text) {

        String rgFace = "\\[[^\\]]+\\]";
        Pattern ptFace = Pattern.compile(rgFace, Pattern.CASE_INSENSITIVE);
        return dealExpression(context, text, ptFace, 0);
    }

    private static SpannableString dealExpression(Context context,
                                                  String text, Pattern patten, int start) {
        SpannableString spannableString = new SpannableString(text);
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            Log.e("FaceUtil","mact");
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            String value = faceMap.get(key);
            Log.e("FaceUtil","mact"+value);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            int resId = context.getResources().getIdentifier(value, "drawable",
                    context.getPackageName());
            Log.e("FaceUtil","mact"+resId);
            // 通过上面匹配得到的字符串来生成图片资源id
            if (resId != 0) {
                Drawable drawable = context.getResources().getDrawable(resId);
                drawable.setBounds(0, 0, DensityUtil.dip2px(context, 26), DensityUtil.dip2px(context, 26));
                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

}