package com.zyloong.springsecurity.demo.entity;

/**
 * 用于接收登录参数
 *
 * @author zyloong
 */
public class AuthenticationBean {
    private String username;
    private String password;

    public AuthenticationBean() {
    }

    public AuthenticationBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
