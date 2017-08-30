package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 女优详请返回
 * Created by scene on 17-8-30.
 */

public class ActorDetailResultInfo implements Serializable {

    /**
     * actor : {"id":18,"name":"天海翼","age":29,"thumb":"/img/2017/08/21d2a230973afbce734dcebbca2238d4.jpeg","sanwei":"99/80/103","weight":0,"view_times":10,"description":"天海翼长得不错","status":1,"create_time":1503643378,"works":[{"title":"1111111","thumb":"/img/2017/08/1e449e224d07c93388bc1cdba89976f1.jpeg"},{"title":"222222222","thumb":"/img/2017/08/a1f54ad16af75d01af5c27b53113df47.jpeg"},{"title":"33333333","thumb":"/img/2017/08/e3b8831b0b9b2fee6de9f5af42b3bcdd.jpeg"}]}
     * gallery : {"data":[{"id":21,"actor_id":18,"thumb":"/img/2017/08/659cf8f59387e137d4b0d144535e3f41.jpeg","view_times":0},{"id":20,"actor_id":18,"thumb":"/img/2017/08/90740e7a8bb051d8a77bbbec430f9c41.jpeg","view_times":0},{"id":19,"actor_id":18,"thumb":"/img/2017/08/aa26d2348307638b01fccffb3e799cbf.jpeg","view_times":0},{"id":18,"actor_id":18,"thumb":"/img/2017/08/e730a07d0f55ffc454c986247cb1a7de.jpeg","view_times":0}]}
     */

    private ActorBean actor;
    private GalleryBean gallery;

    public ActorBean getActor() {
        return actor;
    }

    public void setActor(ActorBean actor) {
        this.actor = actor;
    }

    public GalleryBean getGallery() {
        return gallery;
    }

    public void setGallery(GalleryBean gallery) {
        this.gallery = gallery;
    }

    public static class ActorBean implements Serializable {
        /**
         * id : 18
         * name : 天海翼
         * age : 29
         * thumb : /img/2017/08/21d2a230973afbce734dcebbca2238d4.jpeg
         * sanwei : 99/80/103
         * weight : 0
         * view_times : 10
         * description : 天海翼长得不错
         * status : 1
         * create_time : 1503643378
         * works : [{"title":"1111111","thumb":"/img/2017/08/1e449e224d07c93388bc1cdba89976f1.jpeg"},{"title":"222222222","thumb":"/img/2017/08/a1f54ad16af75d01af5c27b53113df47.jpeg"},{"title":"33333333","thumb":"/img/2017/08/e3b8831b0b9b2fee6de9f5af42b3bcdd.jpeg"}]
         */

        private int id;
        private String name;
        private int age;
        private String thumb;
        private String sanwei;
        private int weight;
        private int view_times;
        private String description;
        private int status;
        private int create_time;
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

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getView_times() {
            return view_times;
        }

        public void setView_times(int view_times) {
            this.view_times = view_times;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public List<WorksBean> getWorks() {
            return works;
        }

        public void setWorks(List<WorksBean> works) {
            this.works = works;
        }

        public static class WorksBean {
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

    public static class GalleryBean implements Serializable {
        private List<DataBean> data;

        private PageInfo info;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public PageInfo getInfo() {
            return info;
        }

        public void setInfo(PageInfo info) {
            this.info = info;
        }

        public static class DataBean {
            /**
             * id : 21
             * actor_id : 18
             * thumb : /img/2017/08/659cf8f59387e137d4b0d144535e3f41.jpeg
             * view_times : 0
             */

            private int id;
            private int actor_id;
            private String thumb;
            private int view_times;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getActor_id() {
                return actor_id;
            }

            public void setActor_id(int actor_id) {
                this.actor_id = actor_id;
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
}
