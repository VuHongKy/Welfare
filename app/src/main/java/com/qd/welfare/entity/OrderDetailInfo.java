package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 订单详情
 * Created by scene on 2017/9/7.
 */

public class OrderDetailInfo implements Serializable {

    /**
     * id : 1
     * order_id : s170907104555000016
     * user_id : 2
     * goods_id : 2
     * price : 3
     * number : 1
     * money : 3
     * status : 2
     * delivery_code :
     * delivery_no :
     * address : xxx
     * username : xxx
     * phone : 1888888888
     * goods_name : 发过爱豹震动喷射枪 女用自慰器自动抽插震动棒  伸缩仿真阳具炮机
     * delivery_money : 0
     * thumb : /img/2017/09/banner@3x.webp
     */

    private int id;
    private String order_id;
    private int user_id;
    private int goods_id;
    private int price;
    private int number;
    private int money;
    private int status;
    private String delivery_code;
    private String delivery_no;
    private String address;
    private String username;
    private String phone;
    private String goods_name;
    private int delivery_money;
    private String thumb;
    private String delivery_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDelivery_code() {
        return delivery_code;
    }

    public void setDelivery_code(String delivery_code) {
        this.delivery_code = delivery_code;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
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

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getDelivery_money() {
        return delivery_money;
    }

    public void setDelivery_money(int delivery_money) {
        this.delivery_money = delivery_money;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }
}
