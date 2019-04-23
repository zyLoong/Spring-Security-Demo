package com.zyloong.springsecurity.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyloong
 */
@RestController
@RequestMapping({"test","api"})
public class TestController {

    @GetMapping(value = "/noPermission")
    public String t1() {
        return "本方法无权限限制，任意用户可访问，即使未登录";
    }

    @GetMapping(value = "/logined")
    public String t2() {
        return "本方法允许任意已登录用户访问";
    }

    @PostMapping(value = "/admin")
    public String t3() {
        return "/admin 本方法允许拥有本方法权限的用户登陆";
    }

    @GetMapping(value = "/user")
    public String t4() {
        return "/user 本方法允许拥有本方法权限的用户登陆";
    }
}
