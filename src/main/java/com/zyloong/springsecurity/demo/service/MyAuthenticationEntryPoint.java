package com.zyloong.springsecurity.demo.service;

import com.zyloong.springsecurity.demo.util.Constants;
import com.zyloong.springsecurity.demo.util.SecurityUtil;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于替换默认的访问被拒绝时的返回信息。
 *
 * 替换默认的{@link ExceptionTranslationFilter}中默认的{@link AuthenticationEntryPoint}
 *
 * @author zyloong
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 当权限决策方法判定不通过时，{@link ExceptionTranslationFilter}的handleSpringSecurityException方法，会根据抛出异常的类型进行相应的处理，
     * 默认情况下会通过{@link LoginUrlAuthenticationEntryPoint#commence(HttpServletRequest, HttpServletResponse, AuthenticationException)}来设置response信息，这里进行了替换了默认的处理，使用自定义的response返回信息
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (authException instanceof InsufficientAuthenticationException) {
            SecurityUtil.response(response, -1, Constants.LOGIN_EXPIRED);
        } else if (authException instanceof Authentication) {
            SecurityUtil.response(response, -1, Constants.AUTH_ERROR);
        }
    }
}
