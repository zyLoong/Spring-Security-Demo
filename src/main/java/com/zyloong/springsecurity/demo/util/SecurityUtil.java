package com.zyloong.springsecurity.demo.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Zhu YiLong
 * @date 2019/04/23
 */
public class SecurityUtil {
    //    保存可以被【任何已登录用户】访问的请求的验证器，不在该列表中的请求都会被拦截验证。
    private static final List<AntPathRequestMatcher> ALLOW_ANY_LOGGED_IN_ACCESS = new ArrayList<>();
    //    保存可以被【任何人】访问的请求的验证器，不在该列表中的请求都会被拦截验证。
    private static final List<AntPathRequestMatcher> ALLOW_ANYONE_ACCESS = new ArrayList<>();

    //    没有登录的情况下，访问请求时 spring security的默认用户名，所以此用户名应该禁用
    public static final String ANONYMOUS_USER = "anonymousUser";

    //    登录过期时间，秒，可以根据需要设置。
    public static final int LOGIN_EXPIRE_TIME = 30 * 24 * 60 * 60;

    static {
        Set<String> set = new HashSet<>();

        set.add("/test/noPermission");
        set.add("/api/noPermission");
        // 无权限限制的接口
        set.forEach(x -> ALLOW_ANYONE_ACCESS.add(new AntPathRequestMatcher(x)));

        set.add("/test/logined");
        set.add("/api/logined");
        // 允许任意已登录用户访问的接口
        set.forEach(x -> ALLOW_ANY_LOGGED_IN_ACCESS.add(new AntPathRequestMatcher(x)));
    }

    /**
     * 判断请求是否可以被任何已登录用户访问。
     */
    public static boolean anyLoggedCanAccess(HttpServletRequest request) {
        return ALLOW_ANY_LOGGED_IN_ACCESS.stream().anyMatch(x -> x.matches(request));
    }

    /**
     * 判断请求是否可以被任何人访问
     */
    public static boolean anyoneCanAccess(HttpServletRequest request) {
        return ALLOW_ANYONE_ACCESS.stream().anyMatch(x -> x.matches(request));
    }

    /**
     * 判断用户登录是否已经过期，如果Redis中没有保存用户登录的Cookie，则表示用户登录已经过期或者未登录
     *
     * @param request HtppServletRequest
     * @return true 已过期， false 未过期
     */
    public static boolean loginExpired(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String sessionId = LoginUtil.getCookie(cookies, Constants.LOGIN_SESSION_ID);
        if (StringUtils.isEmpty(sessionId)) {
            return true;
        }
        String value = RedisUtil.get(sessionId);
        return StringUtils.isBlank(value);
    }

    /**
     * 判断用户是否是登录状态
     */
    public static boolean isUserAlreadyLogin(HttpServletRequest request) {
        return !loginExpired(request);
    }

    /**
     * 设置response返回信息，返回默认的操作成功
     */
    public static void response(HttpServletResponse response) throws IOException {
        response(response, 0, "success");
    }

    /**
     * 设置response返回信息
     *
     * @param httpServletResponse response
     * @param respCode            状态码
     * @param respContent         提示消息
     */
    public static void response(HttpServletResponse httpServletResponse, int respCode, String respContent) throws IOException {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("responseCode", respCode);
        responseMap.put("message", respContent);

        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        String json = JsonUtil.toJson(responseMap);
        if (!StringUtils.isEmpty(json)) {
            httpServletResponse.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        }
        httpServletResponse.getOutputStream().flush();
    }

    /**
     * 获取当前登录的用户名
     */
    public static String getUserName() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (user instanceof User) {
            username = ((User) user).getUsername();
        } else {
            username = user.toString();
        }
        return username;
    }
}
