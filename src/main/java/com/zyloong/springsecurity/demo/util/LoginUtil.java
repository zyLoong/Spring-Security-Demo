package com.zyloong.springsecurity.demo.util;

import javax.servlet.http.Cookie;

/**
 * @author Zhu YiLong
 * @date 2019/04/23
 */
public class LoginUtil {

    public static String getCookie(Cookie[] cookies, String key) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static Cookie getCookieOne(Cookie[] cookies, String key) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
