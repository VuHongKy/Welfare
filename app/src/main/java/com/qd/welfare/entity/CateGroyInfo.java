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
}
