package com.qd.welfare.utils;

/**
 * 倒计时时间工具类
 * Created by scene on 2017/11/10.
 */

public class DateFormatUtils {

    public static String getHoursByNow(long time) {
        time = time * 1000 - System.currentTimeMillis();
        long second = time / 1000;
        long hour = second / 60 / 60;
        long minute = (second - hour * 60 * 60) / 60;
        long sec = (second - hour * 60 * 60) - minute * 60;

        String rHour = "";
        String rMin = "";
        String rSs = "";
        // 时
        if (hour < 10) {
            rHour = "0" + hour;
        } else {
            rHour = hour + "";
        }
        // 分
        if (minute < 10) {
            rMin = "0" + minute;
        } else {
            rMin = minute + "";
        }
        // 秒
        if (sec < 10) {
            rSs = "0" + sec;
        } else {
            rSs = sec + "";
        }
        // return hour + "小时" + minute + "分钟" + sec + "秒";
        return rHour + ":" + rMin + ":" + rSs;
    }


    public static String getHours(long time) {
        long second = time / 1000;
        long hour = second / 60 / 60;
        long minute = (second - hour * 60 * 60) / 60;
        long sec = (second - hour * 60 * 60) - minute * 60;

        String rHour = "";
        String rMin = "";
        String rSs = "";
        // 时
        if (hour < 10) {
            rHour = "0" + hour;
        } else {
            rHour = hour + "";
        }
        // 分
        if (minute < 10) {
            rMin = "0" + minute;
        } else {
            rMin = minute + "";
        }
        // 秒
        if (sec < 10) {
            rSs = "0" + sec;
        } else {
            rSs = sec + "";
        }
        // return hour + "小时" + minute + "分钟" + sec + "秒";
        return rHour + ":" + rMin + ":" + rSs;
    }
}
