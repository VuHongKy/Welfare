package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 女优百科
 * Created by scene on 17-8-29.
 */

public class ActressInfo implements Serializable {


    /**
     * id : 18
     * name : 天海翼
     * age : 29
     * thumb : /img/2017/08/21d2a230973afbce734dcebbca2238d4.jpeg
     * sanwei : 99/80/103
     * works : [{"title":"1111111","thumb":"/img/2017/08/1e449e224d07c93388bc1cdba89976f1.jpeg"},{"title":"222222222","thumb":"/img/2017/08/a1f54ad16af75d01af5c27b53113df47.jpeg"},{"title":"33333333","thumb":"/img/2017/08/e3b8831b0b9b2fee6de9f5af42b3bcdd.jpeg"}]
     */

    private int id;
    private String name;
    private int age;
    private String thumb;
    private String sanwei;
    private List<WorksBean> works;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSanwei() {
        return sanwei;
    }

    public void setSanwei(String sanwei) {
        this.sanwei = sanwei;
    }

    public List<WorksBean> getWorks() {
        return works;
    }

    public void setWorks(List<WorksBean> works) {
        this.works = works;
    }

    public class WorksBean implements Serializable {
        /**
         * title : 1111111
         * thumb : /img/2017/08/1e449e224d07c93388bc1cdba89976f1.jpeg
         */

        private String title;
        private String thumb;

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
}
