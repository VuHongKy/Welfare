package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 创建订单需要的参数
 * Created by scene on 2017/9/7.
 */

public class CreateOrderInfo implements Serializable {
    private int goods_id;
    private int user_id;
    private int pay_type;
    private int number;
    private String address;
    private String username;
    private String phone;

    public CreateOrderInfo(int goods_id, int user_id, int pay_type, int number, String address, String username, String phone) {
        this.goods_id = goods_id;
        this.user_id = user_id;
        this.pay_type = pay_type;
        this.number = number;
        this.address = address;
        this.username = username;
        this.phone = phone;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
