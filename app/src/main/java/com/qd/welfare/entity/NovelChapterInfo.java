package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 小说章节信息
 * Created by scene on 2017/9/6.
 */

public class NovelChapterInfo implements Serializable {

    /**
     * id : 6
     * novel_id : 4
     * title : 第一卷 第一章 桃花源里人家
     * weight : 1
     * view_times : 10
     * star : 5
     * status : 正常
     */

    private int id;
    private int novel_id;
    private String title;
    private int weight;
    private int view_times;
    private int star;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNovel_id() {
        return novel_id;
    }

    public void setNovel_id(int novel_id) {
        this.novel_id = novel_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getView_times() {
        return view_times;
    }

    public void setView_times(int view_times) {
        this.view_times = view_times;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
