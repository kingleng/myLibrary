package com.example.mylibrary.util.FaceUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;


import com.example.mylibrary.R;
import com.example.mylibrary.util.DensityUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leng on 2016/12/16.
 */

public class FaceUtil {

    public static Vector<Integer> faceList = new Vector<Integer>();

    static {
        faceList.clear();
        faceList.add(R.drawable.expression_1);
        faceList.add(R.drawable.expression_2);
        faceList.add(R.drawable.expression_3);
        faceList.add(R.drawable.expression_4);
        faceList.add(R.drawable.expression_5);
        faceList.add(R.drawable.expression_6);
        faceList.add(R.drawable.expression_7);
        faceList.add(R.drawable.expression_8);
        faceList.add(R.drawable.expression_9);
        faceList.add(R.drawable.expression_10);
        faceList.add(R.drawable.expression_11);
        faceList.add(R.drawable.expression_12);
        faceList.add(R.drawable.expression_13);
        faceList.add(R.drawable.expression_14);
        faceList.add(R.drawable.expression_15);
        faceList.add(R.drawable.expression_16);

        faceList.add(R.drawable.expression_20);
        faceList.add(R.drawable.expression_21);
        faceList.add(R.drawable.expression_22);
        faceList.add(R.drawable.expression_23);
        faceList.add(R.drawable.expression_24);
        faceList.add(R.drawable.expression_25);
        faceList.add(R.drawable.expression_26);
        faceList.add(R.drawable.expression_27);
        faceList.add(R.drawable.expression_28);
        faceList.add(R.drawable.expression_29);
        faceList.add(R.drawable.expression_30);
        faceList.add(R.drawable.expression_31);
        faceList.add(R.drawable.expression_32);
        faceList.add(R.drawable.expression_33);
        faceList.add(R.drawable.expression_34);
        faceList.add(R.drawable.expression_35);
        faceList.add(R.drawable.expression_36);
        faceList.add(R.drawable.expression_37);
        faceList.add(R.drawable.expression_38);
        faceList.add(R.drawable.expression_39);
        faceList.add(R.drawable.expression_40);
        faceList.add(R.drawable.expression_41);
        faceList.add(R.drawable.expression_42);
        faceList.add(R.drawable.expression_43);
        faceList.add(R.drawable.expression_44);
        faceList.add(R.drawable.expression_45);
        faceList.add(R.drawable.expression_46);
        faceList.add(R.drawable.expression_47);
        faceList.add(R.drawable.expression_48);
        faceList.add(R.drawable.expression_49);
        faceList.add(R.drawable.expression_50);
        faceList.add(R.drawable.expression_51);
        faceList.add(R.drawable.expression_52);
        faceList.add(R.drawable.expression_53);
        faceList.add(R.drawable.expression_54);
        faceList.add(R.drawable.expression_55);
        faceList.add(R.drawable.expression_56);
        faceList.add(R.drawable.expression_57);
        faceList.add(R.drawable.expression_58);
        faceList.add(R.drawable.expression_59);
        faceList.add(R.drawable.expression_60);
        faceList.add(R.drawable.expression_61);
        faceList.add(R.drawable.expression_62);
        faceList.add(R.drawable.expression_63);
        faceList.add(R.drawable.expression_64);
        faceList.add(R.drawable.expression_65);
        faceList.add(R.drawable.expression_66);
        faceList.add(R.drawable.expression_67);


    }

    public static Map<Integer, String> faceIDTable = new HashMap<Integer, String>();

    static {
        faceIDTable.put(R.drawable.expression_1, "[笑]");
        faceIDTable.put(R.drawable.expression_2, "[调皮]");
        faceIDTable.put(R.drawable.expression_3, "[开心]");
        faceIDTable.put(R.drawable.expression_4, "[笑哭]");
        faceIDTable.put(R.drawable.expression_5, "[鄙视]");
        faceIDTable.put(R.drawable.expression_6, "[呆]");
        faceIDTable.put(R.drawable.expression_7, "[宽慰]");
        faceIDTable.put(R.drawable.expression_8, "[得意]");
        faceIDTable.put(R.drawable.expression_9, "[睡觉]");
        faceIDTable.put(R.drawable.expression_10, "[鬼脸]");
        faceIDTable.put(R.drawable.expression_11, "[大笑]");
        faceIDTable.put(R.drawable.expression_12, "[心心眼]");
        faceIDTable.put(R.drawable.expression_13, "[额]");
        faceIDTable.put(R.drawable.expression_14, "[爱心]");
        faceIDTable.put(R.drawable.expression_15, "[亲亲]");
        faceIDTable.put(R.drawable.expression_16, "[痛哭]");

        faceIDTable.put(R.drawable.expression_20, "[闭嘴]");
        faceIDTable.put(R.drawable.expression_21, "[沉睡]");
        faceIDTable.put(R.drawable.expression_22, "[担心]");
        faceIDTable.put(R.drawable.expression_23, "[冷汗]");
        faceIDTable.put(R.drawable.expression_24, "[惊恐]");
        faceIDTable.put(R.drawable.expression_25, "[生气]");
        faceIDTable.put(R.drawable.expression_26, "[飞吻]");
        faceIDTable.put(R.drawable.expression_27, "[轻松]");
        faceIDTable.put(R.drawable.expression_28, "[假笑]");
        faceIDTable.put(R.drawable.expression_29, "[悲伤]");
        faceIDTable.put(R.drawable.expression_30, "[好吃]");
        faceIDTable.put(R.drawable.expression_31, "[坏笑]");
        faceIDTable.put(R.drawable.expression_32, "[哭]");
        faceIDTable.put(R.drawable.expression_33, "[困]");
        faceIDTable.put(R.drawable.expression_34, "[可怕]");
        faceIDTable.put(R.drawable.expression_35, "[流汗]");
        faceIDTable.put(R.drawable.expression_36, "[生病]");
        faceIDTable.put(R.drawable.expression_37, "[失望]");
        faceIDTable.put(R.drawable.expression_38, "[恶魔]");
        faceIDTable.put(R.drawable.expression_39, "[面无表情]");
        faceIDTable.put(R.drawable.expression_40, "[疲劳]");
        faceIDTable.put(R.drawable.expression_41, "[尴尬]");
        faceIDTable.put(R.drawable.expression_42, "[怒]");
        faceIDTable.put(R.drawable.expression_43, "[伤心]");
        faceIDTable.put(R.drawable.expression_44, "[眨眼]");
        faceIDTable.put(R.drawable.expression_45, "[无辜]");
        faceIDTable.put(R.drawable.expression_46, "[淘气]");
        faceIDTable.put(R.drawable.expression_47, "[痛苦]");
        faceIDTable.put(R.drawable.expression_48, "[晕]");
        faceIDTable.put(R.drawable.expression_49, "[混乱]");
        faceIDTable.put(R.drawable.expression_50, "[OK]");
        faceIDTable.put(R.drawable.expression_51, "[FUCK]");
        faceIDTable.put(R.drawable.expression_52, "[肌肉]");
        faceIDTable.put(R.drawable.expression_53, "[鼓掌]");
        faceIDTable.put(R.drawable.expression_54, "[嘴唇]");
        faceIDTable.put(R.drawable.expression_55, "[蛋糕]");
        faceIDTable.put(R.drawable.expression_56, "[鬼]");
        faceIDTable.put(R.drawable.expression_57, "[火]");
        faceIDTable.put(R.drawable.expression_58, "[礼物]");
        faceIDTable.put(R.drawable.expression_59, "[么么]");
        faceIDTable.put(R.drawable.expression_60, "[啤酒]");
        faceIDTable.put(R.drawable.expression_61, "[祈祷]");
        faceIDTable.put(R.drawable.expression_62, "[钱]");
        faceIDTable.put(R.drawable.expression_63, "[闪亮]");
        faceIDTable.put(R.drawable.expression_64, "[心水]");
        faceIDTable.put(R.drawable.expression_65, "[阳光]");
        faceIDTable.put(R.drawable.expression_66, "[月亮]");
        faceIDTable.put(R.drawable.expression_67, "[炸弹]");

    }

    public static Map<String, Integer> faceTable = new HashMap<String, Integer>();

    static {
        faceTable.put("[笑]", R.drawable.expression_1);
        faceTable.put("[调皮]", R.drawable.expression_2);
        faceTable.put("[开心]", R.drawable.expression_3);
        faceTable.put("[笑哭]", R.drawable.expression_4);
        faceTable.put("[鄙视]", R.drawable.expression_5);
        faceTable.put("[呆]", R.drawable.expression_6);
        faceTable.put("[宽慰]", R.drawable.expression_7);
        faceTable.put("[得意]", R.drawable.expression_8);
        faceTable.put("[睡觉]", R.drawable.expression_9);
        faceTable.put("[鬼脸]", R.drawable.expression_10);
        faceTable.put("[大笑]", R.drawable.expression_11);
        faceTable.put("[心心眼]", R.drawable.expression_12);
        faceTable.put("[额]", R.drawable.expression_13);
        faceTable.put("[爱心]", R.drawable.expression_14);
        faceTable.put("[亲亲]", R.drawable.expression_15);
        faceTable.put("[痛哭]", R.drawable.expression_16);

        faceTable.put("[闭嘴]", R.drawable.expression_20);
        faceTable.put("[沉睡]", R.drawable.expression_21);
        faceTable.put("[担心]", R.drawable.expression_22);
        faceTable.put("[冷汗]", R.drawable.expression_23);
        faceTable.put("[惊恐]", R.drawable.expression_24);
        faceTable.put("[生气]", R.drawable.expression_25);
        faceTable.put("[飞吻]", R.drawable.expression_26);
        faceTable.put("[轻松]", R.drawable.expression_27);
        faceTable.put("[假笑]", R.drawable.expression_28);
        faceTable.put("[悲伤]", R.drawable.expression_29);
        faceTable.put("[好吃]", R.drawable.expression_30);
        faceTable.put("[坏笑]", R.drawable.expression_31);
        faceTable.put("[哭]", R.drawable.expression_32);
        faceTable.put("[困]", R.drawable.expression_33);
        faceTable.put("[可怕]", R.drawable.expression_34);
        faceTable.put("[流汗]", R.drawable.expression_35);
        faceTable.put("[生病]", R.drawable.expression_36);
        faceTable.put("[失望]", R.drawable.expression_37);
        faceTable.put("[恶魔]", R.drawable.expression_38);
        faceTable.put("[面无表情]", R.drawable.expression_39);
        faceTable.put("[疲劳]", R.drawable.expression_40);
        faceTable.put("[尴尬]", R.drawable.expression_41);
        faceTable.put("[怒]", R.drawable.expression_42);
        faceTable.put("[伤心]", R.drawable.expression_43);
        faceTable.put("[眨眼]", R.drawable.expression_44);
        faceTable.put("[无辜]", R.drawable.expression_45);
        faceTable.put("[淘气]", R.drawable.expression_46);
        faceTable.put("[痛苦]", R.drawable.expression_47);
        faceTable.put("[晕]", R.drawable.expression_48);
        faceTable.put("[混乱]", R.drawable.expression_49);
        faceTable.put("[OK]", R.drawable.expression_50);
        faceTable.put("[FUCK]", R.drawable.expression_51);
        faceTable.put("[肌肉]", R.drawable.expression_52);
        faceTable.put("[鼓掌]", R.drawable.expression_53);
        faceTable.put("[嘴唇]", R.drawable.expression_54);
        faceTable.put("[蛋糕]", R.drawable.expression_55);
        faceTable.put("[鬼]", R.drawable.expression_56);
        faceTable.put("[火]", R.drawable.expression_57);
        faceTable.put("[礼物]", R.drawable.expression_58);
        faceTable.put("[么么]", R.drawable.expression_59);
        faceTable.put("[啤酒]", R.drawable.expression_60);
        faceTable.put("[祈祷]", R.drawable.expression_61);
        faceTable.put("[钱]", R.drawable.expression_62);
        faceTable.put("[闪亮]", R.drawable.expression_63);
        faceTable.put("[心水]", R.drawable.expression_64);
        faceTable.put("[阳光]", R.drawable.expression_65);
        faceTable.put("[月亮]", R.drawable.expression_66);
        faceTable.put("[炸弹]", R.drawable.expression_67);
    }


    public static Map<String, String> faceMap = new HashMap<String, String>();

    private static int IMAGEWIDTH = 20;

    static {
        faceMap.put("[笑]", "expression_1");
        faceMap.put("[调皮]", "expression_2");
        faceMap.put("[开心]", "expression_3");
        faceMap.put("[笑哭]", "expression_4");
        faceMap.put("[鄙视]", "expression_5");
        faceMap.put("[呆]", "expression_6");
        faceMap.put("[宽慰]", "expression_7");
        faceMap.put("[得意]", "expression_8");
        faceMap.put("[睡觉]", "expression_9");
        faceMap.put("[鬼脸]", "expression_10");
        faceMap.put("[大笑]", "expression_11");
        faceMap.put("[心心眼]", "expression_12");
        faceMap.put("[额]", "expression_13");
        faceMap.put("[爱心]", "expression_14");
        faceMap.put("[亲亲]", "expression_15");
        faceMap.put("[痛哭]", "expression_16");

        faceMap.put("[闭嘴]", "expression_20");
        faceMap.put("[沉睡]", "expression_21");
        faceMap.put("[担心]", "expression_22");
        faceMap.put("[冷汗]", "expression_23");
        faceMap.put("[惊恐]", "expression_24");
        faceMap.put("[生气]", "expression_25");
        faceMap.put("[飞吻]", "expression_26");
        faceMap.put("[轻松]", "expression_27");
        faceMap.put("[假笑]", "expression_28");
        faceMap.put("[悲伤]", "expression_29");
        faceMap.put("[好吃]", "expression_30");
        faceMap.put("[坏笑]", "expression_31");
        faceMap.put("[哭]", "expression_32");
        faceMap.put("[困]", "expression_33");
        faceMap.put("[可怕]", "expression_34");
        faceMap.put("[流汗]", "expression_35");
        faceMap.put("[生病]", "expression_36");
        faceMap.put("[失望]", "expression_37");
        faceMap.put("[恶魔]", "expression_38");
        faceMap.put("[面无表情]", "expression_39");
        faceMap.put("[疲劳]", "expression_40");
        faceMap.put("[尴尬]", "expression_41");
        faceMap.put("[怒]", "expression_42");
        faceMap.put("[伤心]", "expression_43");
        faceMap.put("[眨眼]", "expression_44");
        faceMap.put("[无辜]", "expression_45");
        faceMap.put("[淘气]", "expression_46");
        faceMap.put("[痛苦]", "expression_47");
        faceMap.put("[晕]", "expression_48");
        faceMap.put("[混乱]", "expression_49");
        faceMap.put("[OK]", "expression_50");
        faceMap.put("[FUCK]", "expression_51");
        faceMap.put("[肌肉]", "expression_52");
        faceMap.put("[鼓掌]", "expression_53");
        faceMap.put("[嘴唇]", "expression_54");
        faceMap.put("[蛋糕]", "expression_55");
        faceMap.put("[鬼]", "expression_53");
        faceMap.put("[火]", "expression_57");
        faceMap.put("[礼物]", "expression_58");
        faceMap.put("[么么]", "expression_59");
        faceMap.put("[啤酒]", "expression_60");
        faceMap.put("[祈祷]", "expression_61");
        faceMap.put("[钱]", "expression_62");
        faceMap.put("[闪亮]", "expression_63");
        faceMap.put("[心水]", "expression_64");
        faceMap.put("[阳光]", "expression_65");
        faceMap.put("[月亮]", "expression_66");
        faceMap.put("[炸弹]", "expression_67");
    }

    /**
     * 截取出代表表情的字符串
     *
     * @param context
     * @param str     用户发出去或者受到的信息
     * @return 转变为可以显示图片的SpanableString
     */
    public static SpannableString initContent(Context context, String str) {
        SpannableString spanStr = null;
        if (context != null && str != null) {
            spanStr = new SpannableString(str);
            String temp = spanStr.toString();
            String[] tempList = temp.split("\\[");
            String ret = "";
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].indexOf("]") >= 0) {

                    String bw = tempList[i]
                            .substring(tempList[i].indexOf("]") + 1);
                    String bn = tempList[i].substring(0, tempList[i]
                            .indexOf("]"));
                    if (faceTable.get(bn) != null) {
                        //通过表情的名称找到对应的图片
                        Drawable d = context.getResources().getDrawable(
                                faceTable.get(bn));
                        d.setBounds(0, 0, d.getIntrinsicWidth(), d
                                .getIntrinsicHeight());
                        ImageSpan span = new ImageSpan(d,
                                ImageSpan.ALIGN_BASELINE);
                        spanStr.setSpan(span, ret.length(), ret.length()
                                        + bn.length() + 2,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        ret += "[" + bn + "]" + bw;
                    }
                } else {
                    ret += tempList[i];
                }
            }
        } else {
            spanStr = new SpannableString("");
        }
        return spanStr;
    }


    public static SpannableString getExpressionString(Context context, String text) {

        String rgFace = "\\[[^\\]]+\\]";
        Pattern ptFace = Pattern.compile(rgFace, Pattern.CASE_INSENSITIVE);
        return dealExpression(context, text, ptFace, 0);
    }

    private static SpannableString dealExpression(Context context,
                                                  String text, Pattern patten, int start) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        } else {
            SpannableString spannableString = new SpannableString(text);
            Matcher matcher = patten.matcher(spannableString);
            while (matcher.find()) {
//                Log.e("FaceUtil", "mact");
                String key = matcher.group();
                if (matcher.start() < start) {
                    continue;
                }
                String value = faceMap.get(key);
//                Log.e("FaceUtil", "mact" + value);
                if (TextUtils.isEmpty(value)) {
                    continue;
                }
                int resId = context.getResources().getIdentifier(value, "drawable",
                        context.getPackageName());
//                Log.e("FaceUtil", "mact" + resId);
                // 通过上面匹配得到的字符串来生成图片资源id
                if (resId != 0) {
                    Drawable drawable = context.getResources().getDrawable(resId);
                    drawable.setBounds(0, 0, DensityUtil.dp2px(context, IMAGEWIDTH), DensityUtil.dp2px(context, IMAGEWIDTH));
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

    public static String[] imagesWidth(Context context, String text) {
        String[] mStrings = new String[2];
        String rgFace = "\\[[^\\]]+\\]";
        Pattern ptFace = Pattern.compile(rgFace, Pattern.CASE_INSENSITIVE);

        SpannableString spannableString = new SpannableString(text);
        Matcher matcher = ptFace.matcher(spannableString);
        int num = 0;
        String ss = "";
        while (matcher.find()) {
            num++;

            String key = matcher.group();
            if (matcher.start() < 0) {
                continue;
            }
            String value = faceMap.get(key);
            if (!TextUtils.isEmpty(value)) {
                ss = ss + key;
            }
        }
        int nnum = num * DensityUtil.dp2px(context, IMAGEWIDTH);
        mStrings[0] = nnum+"";
        mStrings[1] = ss;
//        return num * DensityUtil.dp2px(context, 20);
        return mStrings;
    }

    public static String textWithoutImage(String text) {
        String ss = "";
        String rgFace = "\\[[^\\]]+\\]";
        Pattern ptFace = Pattern.compile(rgFace, Pattern.CASE_INSENSITIVE);

        SpannableString spannableString = new SpannableString(text);
        Matcher matcher = ptFace.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < 0) {
                continue;
            }
            String value = faceMap.get(key);
            if (!TextUtils.isEmpty(value)) {
                ss = ss + key;
            }
        }
        return ss;
    }

    public static int IsImage(String text) {
        int ss = 0;
        String rgFace = "\\[[^\\]]+\\]";
        Pattern ptFace = Pattern.compile(rgFace, Pattern.CASE_INSENSITIVE);

        SpannableString spannableString = new SpannableString(text);
        Matcher matcher = ptFace.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < 0) {
                continue;
            }
            String value = faceMap.get(key);
            if (!TextUtils.isEmpty(value)) {
                ss = key.length();
            }
        }
        return ss;
    }

}