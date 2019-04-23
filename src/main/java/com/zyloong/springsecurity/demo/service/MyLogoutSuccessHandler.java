package com.zyloong.springsecurity.demo.service;

import com.zyloong.springsecurity.demo.util.Constants;
import com.zyloong.springsecurity.demo.util.JsonUtil;
import com.zyloong.springsecurity.demo.util.LoginUtil;
import com.zyloong.springsecurity.demo.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登出成功之后的Handler
 *
 * @author zyloong
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    /**
     * spring security默认退出行为会先清除session，
     * 然后置空SecurityContextHolder中Context中的Authentication，
     * 再然后清除SecurityContextHolder中的 Context
     * 完成以上行为之后会调用LogoutSuccessHandler的onLogoutSuccess方法，该方法默认动作是将用户重定向到/login?logout，
     * <p>
     * 这里使用自定义的LogoutSuccessHandler以清除redis中对应的session数据，即清除保存的对应用户登录状态，然后重定向到登录页面
     * 当退出登录时被调用，删除cookie和redis中的sessionKey，不需要清除Session，session和认证信息已经在此方法调用之前被删除了
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        Cookie[] cookies = request.getCookies();
        Cookie cookie = LoginUtil.getCookieOne(cookies, Constants.LOGIN_SESSION_ID);
        if (cookie != null) {
            String sessionId = cookie.getValue();
            if (StringUtils.isNotEmpty(sessionId) && sessionId.startsWith("login_")) {
                Cookie deleteCookie = new Cookie(cookie.getName(), null);
                deleteCookie.setMaxAge(0);
                deleteCookie.setPath("/");
                response.addCookie(deleteCookie);
                RedisUtil.delete(sessionId);
            }
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("responseCode", 0);
        responseMap.put("message", "登陆成功");
        response.getWriter().print(JsonUtil.toJson(responseMap));
    }
}
