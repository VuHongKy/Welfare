package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 相关推荐
 * Created by scene on 17-8-30.
 */

public class RecommendInfo implements Serializable {

    /**
     * id : 1
     * title : 韩国女团写真
     * thumb : /img/2017/08/b480d4833af20b6c96f08fd5679ebd84.jpeg
     * thumb_shu : /img/2017/08/ac9dc0be2e1e769f2d6e4562a7c78e9b.jpeg
     * type : 1
     * star : 5
     * tags : ["美腿","大长腿"]
     * play_times : 2
     * url : /video/cdn/video/248.mp4
     * duration : 0
     */

    private int id;
    private String title;
    private String thumb;
    private String thumb_shu;
    private int type;
    private int star;
    private int play_times;
    private String url;
    private int duration;
    private List<String> tags;

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

    public String getThumb_shu() {
        return thumb_shu;
    }

    public void setThumb_shu(String thumb_shu) {
        this.thumb_shu = thumb_shu;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getPlay_times() {
        return play_times;
    }

    public void setPlay_times(int play_times) {
        this.play_times = play_times;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
