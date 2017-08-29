package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 服务器返回的配置文件
 * Created by scene on 17-8-29.
 */

public class CommonInfo implements Serializable {

    /**
     * user_agreement : 用户协议
     * file_domain : http://119.23.110.78:8087/
     */

    private String user_agreement;
    private String file_domain;

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
}
