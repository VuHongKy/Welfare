package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 支付结果
 * Created by scene on 2017/9/5.
 */

public class PayResultInfo implements Serializable {


    /**
     * pay_success : true
     * user_id : 2
     * role : 3
     */

    private boolean pay_success;
    private int user_id;
    private int role;

    public boolean isPay_success() {
        return pay_success;
    }

    public void setPay_success(boolean pay_success) {
        this.pay_success = pay_success;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
