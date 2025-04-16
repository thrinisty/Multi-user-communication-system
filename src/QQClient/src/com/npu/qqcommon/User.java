package com.npu.qqcommon;

import java.io.Serializable;

//表示一个用户信息
public class User implements Serializable {
    private static final long serialVersionUID = 1L;//增强兼容性
    private String userId;//用户名
    private String password;

    public User() {
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
