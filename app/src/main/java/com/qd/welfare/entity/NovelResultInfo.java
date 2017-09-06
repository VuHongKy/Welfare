package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 小说返回
 * Created by scene on 2017/9/6.
 */

public class NovelResultInfo  implements Serializable{
    private List<NovelInfo> data;
    private PageInfo info;

    public List<NovelInfo> getData() {
        return data;
    }

    public void setData(List<NovelInfo> data) {
        this.data = data;
    }

    public PageInfo getInfo() {
        return info;
    }

    public void setInfo(PageInfo info) {
        this.info = info;
    }
}
