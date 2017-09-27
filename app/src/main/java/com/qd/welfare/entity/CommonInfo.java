package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 服务器返回的配置文件
 * Created by scene on 17-8-29.
 */

public class CommonInfo implements Serializable {


    /**
     * user_agreement : 用户协议
     * file_domain : https://files.tsacw.com/
     * vip_month_cost : 2
     * vip_year_cost : 3
     * discount_vip_month_cost : 4
     * discount_vip_year_cost : 5
     * try_time:1000
     */

    private String user_agreement;
    private String file_domain;
    private int vip_month_cost;
    private int vip_year_cost;
    private int discount_vip_month_cost;
    private int discount_vip_year_cost;
    private int try_time;

    public String getUser_agreement() {
        return user_agreement;
    }

    public void setUser_agreement(String user_agreement) {
        this.user_agreement = user_agreement;
    }

    public String getFile_domain() {
        return file_domain;
    }

    public void setFile_domain(String file_domain) {
        this.file_domain = file_domain;
    }

    public int getVip_month_cost() {
        return vip_month_cost;
    }

    public void setVip_month_cost(int vip_month_cost) {
        this.vip_month_cost = vip_month_cost;
    }

    public int getVip_year_cost() {
        return vip_year_cost;
    }

    public void setVip_year_cost(int vip_year_cost) {
        this.vip_year_cost = vip_year_cost;
    }

    public int getDiscount_vip_month_cost() {
        return discount_vip_month_cost;
    }

    public void setDiscount_vip_month_cost(int discount_vip_month_cost) {
        this.discount_vip_month_cost = discount_vip_month_cost;
    }

    public int getDiscount_vip_year_cost() {
        return discount_vip_year_cost;
    }

    public void setDiscount_vip_year_cost(int discount_vip_year_cost) {
        this.discount_vip_year_cost = discount_vip_year_cost;
    }

    public int getTry_time() {
        return try_time;
    }

    public void setTry_time(int try_time) {
        this.try_time = try_time;
    }
}
