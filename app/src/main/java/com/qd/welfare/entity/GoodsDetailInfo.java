package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情
 * Created by scene on 2017/9/6.
 */

public class GoodsDetailInfo implements Serializable {


    /**
     * id : 1
     * name : VK英国卫裤
     * sex : 女
     * description : 英国第十一代能量内裤
     * thumb : /goods/1/1.webp
     * price : 138
     * hits : 65958
     * images : ["/goods/1/1.webp","/goods/1/2.webp","/goods/1/3.webp","/goods/1/4.webp","/goods/1/5.webp","/goods/1/6.webp","/goods/1/7.webp","/goods/1/8.webp","/goods/1/9.webp","/goods/1/10.webp","/goods/1/11.webp"]
     * sales : 35684
     * address : 重庆南坪
     * delivery_money : 0
     * freight : 0.00
     */

    private int id;
    private String name;
    private String sex;
    private String description;
    private String thumb;
    private int price;
    private int hits;
    private int sales;
    private String address;
    private int delivery_money;
    private String freight;
    private List<String> images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDelivery_money() {
        return delivery_money;
    }

    public void setDelivery_money(int delivery_money) {
        this.delivery_money = delivery_money;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
