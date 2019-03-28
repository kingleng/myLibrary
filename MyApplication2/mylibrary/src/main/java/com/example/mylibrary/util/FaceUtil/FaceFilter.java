package com.example.mylibrary.util.FaceUtil;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leng on 2017/1/13.
 */

/**
 * 系统自带的emoji表情过滤器
 */
public class FaceFilter {

    public static Context context = null;

    private static InputFilter emojiFilter = new InputFilter() {

        Pattern emoji = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                if(context!=null){
                    Toast.makeText(context,"对不起，暂不支持系统表情",Toast.LENGTH_SHORT).show();
                }
                return "";

            }
            return null;
        }
    };

    public static InputFilter[] emojiFilters = {emojiFilter};
}
