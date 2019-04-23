package com.zyloong.springsecurity.demo.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zyloong
 */
@Service
public class SecurityService {
    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 将指定用户的权限信息放入session。
     * 如果用户名不存在，或者用户被禁用，本方法将会抛出对应的异常。
     *
     * @throws UsernameNotFoundException 用户名不存在
     * @throws LockedException           用户被禁用
     */
    void saveUserPermissions2Session(HttpServletRequest request, String username) throws UsernameNotFoundException, LockedException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("用户名不存在:" + username);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        User principal = new User(userDetails.getUsername(), userDetails.getPassword(), true, true, true, true, userDetails.getAuthorities());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, userDetails.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authentication.setDetails(details);

        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
