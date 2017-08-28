package com.qd.welfare.config;

/**
 * Case By:项目配置文件
 * package:wiki.scene.shop.config
 * Author：scene on 2017/6/29 16:38
 */

public class AppConfig {
    public static final int CHOOSE_AVATER_REQUEST_CODE = 1001;
    public static final int ADD_ADDRESS_REQUEST_CODE = 1002;
    public static final int ORDER_DETAIL_TO_PAY_REQUEST_CODE = 1003;
    public static final int SHARE_ORDER_REQUEST_CODE = 1004;
    public static final String WX_APPKEY = "wx4d1ef7a0167fde7e";
    public static final String WX_APPSECRET = "18af9d20dddaffd4217940a8b407469b";
    public static final String QQ_APPKEY = "1106258470";
    public static final String QQ_APPSECRET = "YED2DoxyDzJGlmVX";
    public static final String WEIBO_APPKEY = "4230088431";
    public static final String WEIBO_APPSECRET = "18af9d20dddaffd4217940a8b407469b";
    public static final String WEIBO_REDIRECT_URL = "http://sns.whalecloud.com";

    public static final int PAY_TYPE_BALANCE = 1;
    public static final int PAY_TYPE_WECHAT = 2;
    public static final int PAY_TYPE_ALPAY = 3;
    public static final int DEFAULT_PAY_WAY = PAY_TYPE_WECHAT;

    public static final int GET_WINNER_NOTICE_DELAY = 60 * 1000;
    public static final int GET_DANMU_DELAY = 60 * 1000;
    public static final int SHOW_DANMU_DELAY = 3 * 1000;
    public static final int HIDE_DANMU_DELAY = 10 * 1000;
}
