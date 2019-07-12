package com.example.leng.myapplication2.ui.tools;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 17092234 on 2018/3/8.
 */

public class TimeUtil {

    public static String getTimeTypeTwo(String time0) {

        if(TextUtils.isEmpty(time0)){
            return "";
        }

        String time;
        if(time0.length()>19){
            time = time0.substring(0,19);
        } else {
            time = time0;
        }

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            Date date = df.parse(time);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

            if(day<1){
                if (hour < 1) {
                    if (min < 1){
                        return "刚刚";
                    } else {
                        return min + "分钟前";
                    }
                } else if (day < 1) {
                    return hour + "小时前";
                }
            }else{
//                if (day < 3) {
//                    return day + "天前";
//                } else {
//                    return "3天前";
//                }
                return day + "天前";
            }



        } catch (ParseException e) {
            return "";
        }

        return "";

    }

    public static String getTimeTypeOne(String time) {

        if(TextUtils.isEmpty(time)){
            return "未知";
        }

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            Date date = df.parse(time);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            if (isToday(time)) {
                if (hour < 1) {
                    if (min < 1){
                        return "刚刚";
                    } else {
                        return min + "分钟前";
                    }
                } else if (day < 1) {
                    return hour + "小时前";
                }
            } else if(isYesterday(time)){
                return "1天前";
            }else if (day < 3) {
                return day + "天前";
            } else {
                return "3天前";
            }

        } catch (ParseException e) {
            return "未知";
        }

        return "未知";

    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<>();

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

}
