package com.qd.welfare.utils;

import android.text.TextUtils;

/**
 * Case By:项目的一些工具类
 * package:wiki.scene.shop.utils
 * Author：scene on 2017/7/6 12:02
 */

public class AppUtils {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
}
