package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 图片信息
 * Created by scene on 17-8-30.
 */

public class GalleryInfo implements Serializable {

    /**
     * id : 24
     * cate_id : 18
     * thumb : /img/2017/08/2b6b5057ef0191439d689609d6a7e14e.jpeg
     * view_times : 0
     */

    private int id;
    private int cate_id;
    private String thumb;
    private int view_times;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
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
}
