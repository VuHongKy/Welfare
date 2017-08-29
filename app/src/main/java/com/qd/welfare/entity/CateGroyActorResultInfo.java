package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分类进去的界面
 * Created by scene on 17-8-29.
 */

public class CateGroyActorResultInfo implements Serializable {
    private List<CateGroyActorInfo> data;
    private PageInfo info;

    public List<CateGroyActorInfo> getData() {
        return data;
    }

    public void setData(List<CateGroyActorInfo> data) {
        this.data = data;
    }

    public PageInfo getInfo() {
        return info;
    }

    public void setInfo(PageInfo info) {
        this.info = info;
    }

    public class CateGroyActorInfo implements Serializable {

        /**
         * id : 18
         * name : 白咲碧
         * thumb : /img/2017/08/c58d3bea89db5df477ede90fb50b6617.jpeg
         * view_times : 1
         */

        private int id;
        private String name;
        private String thumb;
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
}
