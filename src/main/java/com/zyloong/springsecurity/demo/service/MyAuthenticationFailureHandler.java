package com.zyloong.springsecurity.demo.service;

import com.zyloong.springsecurity.demo.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当登录验证失败时使用的处理器
 *
 * @author zyloong
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final static Logger logger = LoggerFactory.getLogger(MyAuthenticationFailureHandler.class);

    /**
     * 登录验证失败，返回提示信息
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof AccountExpiredException) {
            SecurityUtil.response(response, -1, "账号已过期");
            return;
        }
        if (exception instanceof BadCredentialsException) {
            SecurityUtil.response(response, -1, "用户名或密码错误");
            return;
        }
        if (exception instanceof AuthenticationCredentialsNotFoundException) {
            SecurityUtil.response(response, -1, "未发现登陆凭证");
            return;
        }
        if (exception instanceof LockedException) {
            SecurityUtil.response(response, -1, "账号被锁定");
            return;
        }
        if (exception instanceof DisabledException) {
            SecurityUtil.response(response, -1, "账号未启用");
            return;
        }
        SecurityUtil.response(response, -1, "其他错误");
    }
}
