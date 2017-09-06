package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 小说详情
 * Created by scene on 2017/9/6.
 */

public class NovelDetailInfo implements Serializable {

    /**
     * chapter : 第一卷 第一章 桃花源里人家
     * content : 岭南邵州东北二十余里处，有一座无名......故而沿用时下读者习惯的称呼。
     */

    private String chapter;
    private String content;

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
