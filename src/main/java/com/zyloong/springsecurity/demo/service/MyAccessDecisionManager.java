package com.zyloong.springsecurity.demo.service;


import com.zyloong.springsecurity.demo.entity.MyGrantedAuthority;
import com.zyloong.springsecurity.demo.util.Constants;
import com.zyloong.springsecurity.demo.util.LoginUtil;
import com.zyloong.springsecurity.demo.util.RedisUtil;
import com.zyloong.springsecurity.demo.util.SecurityUtil;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;


/**
 * 权限决策类。
 * @author zyloong
 */
@Service
public class MyAccessDecisionManager implements AccessDecisionManager {

    @Resource
    private SecurityService securityService;

    /**
     * 判定是否拥有权限的决策方法，如果抛出异常，代表无权访问或没有登录。如果正常返回，则代表有访问权限
     * <p>
     * 抛出异常后，会在{@link ExceptionTranslationFilter#handleSpringSecurityException(HttpServletRequest, HttpServletResponse, FilterChain, RuntimeException)} 根据异常类型进行相应的处理
     *
     * @param authentication   用户所拥有的权限集合，在CustomUserService中添加到用户信息中
     * @param object           包含客户端发起的请求的requset信息，可转换为 HttpServletRequest
     * @param configAttributes 为{@link MyInvocationSecurityMetadataSourceService#getAllConfigAttributes()}返回的结果，此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。没有使用。
     * @throws AccessDeniedException               没有访问权限
     * @throws InsufficientAuthenticationException 没有登陆（接口中倒貌似不是这个意思，这里这么用了）
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();

        // 如果接口允许任意访问（即使未登录），则直接放行
        if (SecurityUtil.anyoneCanAccess(request)) {
            return;
        }

        // 如果用户已登陆，并且该接口允许任意已登陆用户访问，则直接放行
        if (!SecurityUtil.ANONYMOUS_USER.equals(SecurityUtil.getUserName()) && SecurityUtil.anyLoggedCanAccess(request)) {
            return;
        }

        // 检查用户登陆是否已过期。
        if (SecurityUtil.loginExpired(request)) {
            throw new InsufficientAuthenticationException("用户登陆过期，请重新登陆");
        }

        // 因为用户登录后Session保存用户登陆信息，当用户关闭浏览器后，再重新打开浏览器会重新创建一个新的Session，造成原Session失效，从而需要重新登陆。
        // 所以用户登陆后将用户名和Cookie保存到Redis中（保存一定时间），当用户重新打开浏览器时，虽然Session是新的，但是Cookie还在，
        // 通过比对请求中的Cookie和Redis中保存的Cookie，如果一致，则判断用户之前已经登陆了，则把用户权限信息保存到当前Session中。
        if (SecurityUtil.ANONYMOUS_USER.equals(SecurityUtil.getUserName()) && SecurityUtil.isUserAlreadyLogin(request)) {

            String redisUserLoginKey = LoginUtil.getCookie(request.getCookies(), Constants.LOGIN_SESSION_ID);
            String username = RedisUtil.get(redisUserLoginKey);
            securityService.saveUserPermissions2Session(request, username);
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }

        // 权限验证
        AntPathRequestMatcher matcher;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof MyGrantedAuthority) {
                String url = ((MyGrantedAuthority) authority).getUrl();
                String method = ((MyGrantedAuthority) authority).getMethod();
                matcher = new AntPathRequestMatcher(url);
                if (matcher.matches(request)) {
                    if (method.equalsIgnoreCase(request.getMethod())) {
                        return;
                    }
                }
            } else if (authority instanceof SimpleGrantedAuthority) {
//                是否未登录
                if (Constants.ROLE_ANONYMOUS.equals(authority.getAuthority())) {
                    throw new InsufficientAuthenticationException("会话过期，请重新登录");
                }
            }
        }

//        无权访问
        throw new AccessDeniedException("该用户没有此操作权限");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
