package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 支付信息
 * Created by scene on 2017/9/4.
 */

public class PayInfo implements Serializable {

    /**
     * status : true
     * url : https://qr.alipay.com/bax03942tiiungrt0uug20bf
     * qr_url : https://pay.swiftpass.cn/pay/qrcode?uuid=https%3A%2F%2Fqr.alipay.com%2Fbax03942tiiungrt0uug20bf
     * api_type : 4
     * order_id : 25
     */

    private boolean status;
    private String url;
    private String qr_url;
    private int api_type;
    private int order_id;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQr_url() {
        return qr_url;
    }

    public void setQr_url(String qr_url) {
        this.qr_url = qr_url;
    }

    public int getApi_type() {
        return api_type;
    }

    public void setApi_type(int api_type) {
        this.api_type = api_type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
