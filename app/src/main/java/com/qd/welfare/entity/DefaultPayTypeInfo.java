package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 默认支付方式
 * Created by scene on 2017/9/5.
 */

public class DefaultPayTypeInfo implements Serializable {
    private int default_pay_type;

    public int getDefault_pay_type() {
        return default_pay_type;
    }

    public void setDefault_pay_type(int default_pay_type) {
        this.default_pay_type = default_pay_type;
    }
}
