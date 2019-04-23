package com.zyloong.springsecurity.demo.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * 权限具体到每一个接口，这个类用于描述一个接口，包括接口请求 url 和 请求方法
 *
 * @author zyloong
 */
public class MyGrantedAuthority implements GrantedAuthority {
    private String url;
    private String method;

    @Override
    public String getAuthority() {
        return this.url + ";" + this.method;
    }


    public MyGrantedAuthority(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
