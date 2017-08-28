package com.qd.welfare.http.api;

import com.qd.welfare.App;
import com.qd.welfare.utils.MD5Util;

import java.util.HashMap;


/**
 * Case By:API
 * package:wiki.scene.shop.http.api
 * Author：scene on 2017/6/27 12:51
 */

public class ApiUtil {
    private static final String SIGN_KEY = "045448f765b0c0592563123a2652fb63";
    public static final String API_PRE = "http://119.23.110.78:8082";

    /**
     * Case By:创建参数基础信息
     * Author: scene on 2017/5/19 13:19
     */
    public static HashMap<String, String> createParams() {
        HashMap<String, String> params = new HashMap<>();
        long timestamp = System.currentTimeMillis();
        params.put("agent_id", App.CHANNEL_ID + "");
        params.put("timestamp", timestamp + "");
        params.put("signature", getSign(App.CHANNEL_ID + "", timestamp + ""));
        params.put("app_type", "1");
        params.put("uuid", App.UUID);
        params.put("version", App.versionCode + "");
        return params;
    }

    /**
     * Case By:获取sign
     * Author: scene on 2017/5/19 13:19
     */
    private static String getSign(String agent_id, String timestamp) {
        return MD5Util.string2Md5(MD5Util.string2Md5(agent_id + timestamp + 1 + App.UUID + App.versionCode, "UTF-8") + SIGN_KEY, "UTF-8");
    }
}
