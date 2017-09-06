package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 分类
 * Created by scene on 17-8-29.
 */

public class CateGroyInfo implements Serializable {

    /**
     * id : 18
     * title : 日韩专区
     * thumb : /img/2017/08/2bfda8f6b03540ace6b43a79322bdfa4.jpeg
     */

    private int id;
    private String title;
    private String thumb;
    private int view_times;
    private int update_to;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getView_times() {
        return view_times;
    }

    public void setView_times(int view_times) {
        this.view_times = view_times;
    }

    public int getUpdate_to() {
        return update_to;
    }

    public void setUpdate_to(int update_to) {
        this.update_to = update_to;
    }
}
