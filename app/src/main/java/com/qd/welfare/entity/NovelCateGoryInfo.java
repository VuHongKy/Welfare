package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 小说分类
 * Created by scene on 2017/9/8.
 */

public class NovelCateGoryInfo implements Serializable {

    /**
     * id : 1
     * name : yanqing
     * title : 言情
     * pid : 0
     * description : 言情小说
     * icon :
     */

    private int id;
    private String name;
    private String title;
    private int pid;
    private String description;
    private String icon;
    private int total;
    private int view_times;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getView_times() {
        return view_times;
    }

    public void setView_times(int view_times) {
        this.view_times = view_times;
    }
}
