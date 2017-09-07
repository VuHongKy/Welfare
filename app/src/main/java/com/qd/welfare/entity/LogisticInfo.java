package com.qd.welfare.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Case By:物流信息
 * package:com.mzhguqvn.mzhguq.base
 * Author：scene on 2017/5/11 12:57
 */

public class LogisticInfo implements Serializable {


    /**
     * message : ok
     * nu : 450520862576
     * ischeck : 1
     * condition : F00
     * com : zhongtong
     * status : 200
     * state : 3
     * data : [{"time":"2017-08-24 18:19:44","ftime":"2017-08-24 18:19:44","context":"[重庆市] [重庆江北五里店]的派件已签收 感谢使用中通快递,期待再次为您服务!","location":"重庆江北五里店"},{"time":"2017-08-24 13:58:09","ftime":"2017-08-24 13:58:09","context":"[重庆市] [重庆江北五里店]的江立正在第1次派件 电话:18996206579 请保持电话畅通、耐心等待","location":"重庆江北五里店"},{"time":"2017-08-24 13:10:57","ftime":"2017-08-24 13:10:57","context":"[重庆市] 快件到达 [重庆江北五里店]","location":"重庆江北五里店"},{"time":"2017-08-24 09:17:01","ftime":"2017-08-24 09:17:01","context":"[重庆市] 快件离开 [重庆]已发往[重庆江北五里店]","location":"重庆"},{"time":"2017-08-24 08:47:56","ftime":"2017-08-24 08:47:56","context":"[重庆市] 快件到达 [重庆]","location":"重庆"},{"time":"2017-08-23 02:37:42","ftime":"2017-08-23 02:37:42","context":"[泉州市] 快件离开 [泉州中转部]已发往[重庆]","location":"泉州中转部"},{"time":"2017-08-23 02:36:58","ftime":"2017-08-23 02:36:58","context":"[泉州市] 快件到达 [泉州中转部]","location":"泉州中转部"},{"time":"2017-08-22 21:47:55","ftime":"2017-08-22 21:47:55","context":"[泉州市] 快件离开 [安海]已发往[重庆]","location":"安海"},{"time":"2017-08-22 20:47:47","ftime":"2017-08-22 20:47:47","context":"[泉州市] 快件到达 [安海]","location":"安海"},{"time":"2017-08-22 18:57:11","ftime":"2017-08-22 18:57:11","context":"[泉州市] [晋江内坑分部]的111佳草集已收件 电话:13859794733","location":"晋江内坑分部"}]
     */

    private String message;
    private String nu;
    private String ischeck;
    private String condition;
    private String com;
    private String status;
    private String state;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * time : 2017-08-24 18:19:44
         * ftime : 2017-08-24 18:19:44
         * context : [重庆市] [重庆江北五里店]的派件已签收 感谢使用中通快递,期待再次为您服务!
         * location : 重庆江北五里店
         */

        private String time;
        private String ftime;
        private String context;
        private String location;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
