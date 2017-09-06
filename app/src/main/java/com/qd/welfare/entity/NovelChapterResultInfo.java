package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 章节列表返回
 * Created by scene on 2017/9/6.
 */

public class NovelChapterResultInfo implements Serializable {
    private List<NovelChapterInfo> data;
    private PageInfo info;

    public List<NovelChapterInfo> getData() {
        return data;
    }

    public void setData(List<NovelChapterInfo> data) {
        this.data = data;
    }

    public PageInfo getInfo() {
        return info;
    }

    public void setInfo(PageInfo info) {
        this.info = info;
    }
}
