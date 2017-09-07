package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 订单列表
 * Created by scene on 2017/9/7.
 */

public class OrderInfo implements Serializable {

    /**
     * status:1
     * order_id : s170907104555000016
     * user_id : 2
     * goods_id : 2
     * price : 3
     * number : 1
     * money : 3
     * goods_name : 发过爱豹震动喷射枪 女用自慰器自动抽插震动棒  伸缩仿真阳具炮机
     * delivery_money : 0
     * thumb:
     */
    private int status;
    private String order_id;
    private int user_id;
    private int goods_id;
    private int price;
    private int number;
    private int money;
    private String goods_name;
    private int delivery_money;
    private String thumb;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
