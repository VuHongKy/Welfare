package com.qd.welfare.config;

/**
 * Case By:项目配置文件
 * package:wiki.scene.shop.config
 * Author：scene on 2017/6/29 16:38
 */

public class AppConfig {

    public static final int PAY_TYPE_WECHAT = 1;
    public static final int PAY_TYPE_ALPAY = 2;
    public static int DEFAULT_PAY_WAY = PAY_TYPE_WECHAT;

    public static final int VIP_MONTH = 1;
    public static final int VIP_YEAR = 2;
    public static final int VIP_MONTH_BACK = 3;
    public static final int VIP_YEAR_BACK = 4;

    public static final int API_TYPE_WX_SCAN = 1;
    public static final int API_TYPE_WX_GZH_SCAN = 2;
    public static final int API_TYPE_WX_GZH_CHANGE = 3;
    public static final int API_TYPE_ALIPAY_SCAN = 4;

}
