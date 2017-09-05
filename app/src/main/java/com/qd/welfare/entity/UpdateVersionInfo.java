package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 版本更新
 * Created by scene on 2017/9/5.
 */

public class UpdateVersionInfo implements Serializable {

    /**
     * url : http://119.23.110.78:8087//apk/宅男福利_1001_1001_1.apk
     * version : 1
     */

    private String url;
    private int version;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
