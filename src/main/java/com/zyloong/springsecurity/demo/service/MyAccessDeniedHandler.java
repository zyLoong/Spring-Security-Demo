package com.zyloong.springsecurity.demo.service;

import com.zyloong.springsecurity.demo.util.Constants;
import com.zyloong.springsecurity.demo.util.SecurityUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求被拒绝时使用的处理器
 * @author zyloong
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 如果用户访问被拒绝，调用此方法，默认情况下如果用户没有权限访问，浏览器接收的数据是 403 HTTP状态码,
     * 这里替代了默认的Handler，不返回403状态码，而是返回自定义提示信息
     * <p>
     * 另外：权限判断是在{@link MyAccessDecisionManager}中处理的，如果没有权限访问会抛出异常，异常的处理在{@link ExceptionTranslationFilter} 的handleSpringSecurityException方法。如果上面两个判断都没有通过，就会调用本方法
     *
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        if (!response.isCommitted()) {
            SecurityUtil.response(response, -1, Constants.AUTH_ERROR);
        }
    }
}
