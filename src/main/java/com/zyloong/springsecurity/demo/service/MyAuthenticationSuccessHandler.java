package com.zyloong.springsecurity.demo.service;

import com.zyloong.springsecurity.demo.dao.UserDao;
import com.zyloong.springsecurity.demo.util.Constants;
import com.zyloong.springsecurity.demo.util.RedisUtil;
import com.zyloong.springsecurity.demo.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录认证成功之后的处理器
 *
 * @author zyloong
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    private UserDao userDao;

    /**
     * 登录认证成功之后的后续处理
     * 设置Redis和Cookie，更新数据库中用户登录时间
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        HttpSession session = request.getSession();
        String sessionId = session.getId();

        // Constants.LOGIN_SESSION_ID 是保存在Redis中的，用于保存用户登陆状态，因为用户关闭再重新打开浏览器后Session会不同，所以不能用Session保存登陆状态。
        Cookie cookie = new Cookie(Constants.LOGIN_SESSION_ID, "login_" + sessionId);
        cookie.setMaxAge(SecurityUtil.LOGIN_EXPIRE_TIME);
        cookie.setPath("/");
        response.addCookie(cookie);
        String username = SecurityUtil.getUserName();
        RedisUtil.setWithExpire("login_" + sessionId, username, SecurityUtil.LOGIN_EXPIRE_TIME);

        SecurityUtil.response(response);
    }
}
