package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 支付结果
 * Created by scene on 2017/9/5.
 */

public class PayResultInfo implements Serializable {

    /**
     * pay_success : false
     */

    private boolean pay_success;

    public boolean isPay_success() {
        return pay_success;
    }

    public void setPay_success(boolean pay_success) {
        this.pay_success = pay_success;
    }
}
