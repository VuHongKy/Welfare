package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 弹幕数据
 * Created by scene on 2017/9/30.
 */

public class DanmuInfo implements Serializable {
    private String avatar;
    private String content;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
