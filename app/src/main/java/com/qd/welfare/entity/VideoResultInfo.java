package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 视频返回
 * Created by scene on 17-8-30.
 */

public class VideoResultInfo implements Serializable {
    private VideoIndexInfo try_banner;
    private List<VideoIndexInfo> try_other;
    private List<VideoIndexInfo> vip_other;

    public VideoIndexInfo getTry_banner() {
        return try_banner;
    }

    public void setTry_banner(VideoIndexInfo try_banner) {
        this.try_banner = try_banner;
    }

    public List<VideoIndexInfo> getTry_other() {
        return try_other;
    }

    public void setTry_other(List<VideoIndexInfo> try_other) {
        this.try_other = try_other;
    }

    public List<VideoIndexInfo> getVip_other() {
        return vip_other;
    }

    public void setVip_other(List<VideoIndexInfo> vip_other) {
        this.vip_other = vip_other;
    }

    public class VideoIndexInfo implements Serializable {
        private String title;
        private int show_type;
        private List<VideoInfo> video;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getShow_type() {
            return show_type;
        }

        public void setShow_type(int show_type) {
            this.show_type = show_type;
        }

        public List<VideoInfo> getVideo() {
            return video;
        }

        public void setVideo(List<VideoInfo> video) {
            this.video = video;
        }
    }
}
