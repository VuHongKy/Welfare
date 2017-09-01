package com.qd.welfare.entity;

import java.io.Serializable;

/**
 * Created by scene on 2017/9/1.
 */

public class UserInfo implements Serializable {

    /**
     * id : 1
     * login_times : 2
     * role : 1// [1 => '游客', 2 => '包月VIP', 3 => '包年VIP']
     */

    private int id;
    private int login_times;
    private int role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLogin_times() {
        return login_times;
    }

    public void setLogin_times(int login_times) {
        this.login_times = login_times;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
