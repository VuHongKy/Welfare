package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 开通会员通知
 * Created by scene on 2017/9/30.
 */

public class OpenVipInfo implements Serializable {

    /**
     * user_id : 534599
     * vip_type : 1
     */

    private int user_id;
    private int vip_type;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getVip_type() {
        return vip_type;
    }

    public void setVip_type(int vip_type) {
        this.vip_type = vip_type;
    }
}
