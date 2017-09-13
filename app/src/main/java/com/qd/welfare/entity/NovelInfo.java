package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 小说
 * Created by scene on 2017/9/6.
 */

public class NovelInfo implements Serializable {

    /**
     * id : 4
     * title : 醉枕江山
     * thumb : /img/2017/09/d6f8d4f8e91b20a82a945f6aee7e862b.jpeg
     * tags : 醉枕江山,醉枕江山小说
     * total_word : 13444530
     * description : 女帝武曌日月凌空，上官婉儿称量天下，太平公主难太平，李家三郎真隆基，才子、佳人、屠狗辈！醉卧枕江山，谈笑望乾坤！
     * author : 月关
     * status : 连载中
     */

    private int id;
    private String title;
    private String thumb;
    private List<String> tags;
    private double total_word;
    private String description;
    private String author;
    private String status;
    private int chapters_id;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public double getTotal_word() {
        return total_word;
    }

    public void setTotal_word(double total_word) {
        this.total_word = total_word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getChapters_id() {
        return chapters_id;
    }

    public void setChapters_id(int chapters_id) {
        this.chapters_id = chapters_id;
    }
}
