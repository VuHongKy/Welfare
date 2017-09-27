package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * 视频详情
 * Created by scene on 2017/9/1.
 */

public class VideoDetailInfo implements Serializable {

    /**
     * id : 1
     * title : 韩国女团写真
     * position_id : 1
     * thumb : /img/2017/08/b480d4833af20b6c96f08fd5679ebd84.jpeg
     * thumb_shu : /img/2017/08/ac9dc0be2e1e769f2d6e4562a7c78e9b.jpeg
     * type : 1
     * star : 5
     * tags : 美腿,大长腿
     * url : /video/cdn/video/248.mp4
     * weight : 0
     * play_times : 0
     * status : 1
     * create_time : 0
     * duration : 0
     */

    private int id;
    private String title;
    private int position_id;
    private String thumb;
    private String thumb_shu;
    private int type;
    private int star;
    private String tags;
    private String url;
    private int weight;
    private int play_times;
    private int status;
    private long create_time;
    private long duration;
    private String short_video_url;

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

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPlay_times() {
        return play_times;
    }

    public void setPlay_times(int play_times) {
        this.play_times = play_times;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getShort_video_url() {
        return short_video_url;
    }

    public void setShort_video_url(String short_video_url) {
        this.short_video_url = short_video_url;
    }
}
