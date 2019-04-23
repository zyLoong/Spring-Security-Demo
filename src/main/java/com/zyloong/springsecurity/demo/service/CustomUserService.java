package com.zyloong.springsecurity.demo.service;

import com.zyloong.springsecurity.demo.dao.PermissionDao;
import com.zyloong.springsecurity.demo.dao.UserDao;
import com.zyloong.springsecurity.demo.entity.MyGrantedAuthority;
import com.zyloong.springsecurity.demo.entity.PermissionEntity;
import com.zyloong.springsecurity.demo.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户登录服务
 *
 * @author zyloong
 */
@Service
public class CustomUserService implements UserDetailsService {
    private final static Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    @Resource
    private UserDao userDao;

    @Resource
    private PermissionDao permissionDao;

    /**
     * 用户登陆时，会调用这个方法，从数据库中根据用户名查询用户，如果查询不到则抛出异常
     * 如果用户的state为false，表示用户被锁定，抛出异常
     * 然后根据用户ID从数据库中查询到用户所拥有的权限信息，并封装返回
     *
     * @param username 用户名
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userDao.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " do not exist");
        }

        Set<PermissionEntity> permissionSet = permissionDao.listByUserId(user.getId());
        Set<MyGrantedAuthority> authorities = getAuthorities(permissionSet);
        assert authorities != null;

        return new User(user.getUsername(), user.getPassword(), user.isStatus(), true, true, true, authorities);
    }

    /**
     * 将从数据库中获取的 PermissionEntity 集合，解析为供Spring Security使用的 MyGrantedAuthority 集合
     * MyGrantedAuthority 继承于 GrantedAuthority
     */
    private Set<MyGrantedAuthority> getAuthorities(Collection<PermissionEntity> permissionEntities) {
        Set<MyGrantedAuthority> authorities = new HashSet<>();
        if (permissionEntities == null || permissionEntities.isEmpty()) {
            return authorities;
        }
        MyGrantedAuthority authority;
        for (PermissionEntity permissionEntity : permissionEntities) {
            String prefixStr = permissionEntity.getPrefix();
            String urlStr = permissionEntity.getUrl();

            if (StringUtils.isEmpty(urlStr)) {
                //            urlStr是Controller中请求方法上的URI地址，不应该为空
                logger.error("permissionEntity url is empty:" + permissionEntity);
                return null;
            } else if (StringUtils.isEmpty(prefixStr)) {
                //            prefixStr是Controller类上的URL，可能为空
                Set<MyGrantedAuthority> subAuthorities = getAuthoritiesWithEmptyPrefix(permissionEntity);
                authorities.addAll(subAuthorities);
            } else {
                //                两者都不为空
                String[] prefixs = prefixStr.split(",");
                String[] urls = urlStr.split(",");
                for (String prefix : prefixs) {
                    for (String url : urls) {
                        String wholeUrl = prefix + url;
                        authority = new MyGrantedAuthority(wholeUrl, permissionEntity.getMethod());
                        authorities.add(authority);
                    }
                }
            }
        }
        return authorities;
    }

    /**
     * 将一个prefix为空,url不为空的permissionEntity解析为MyGrantedAuthority集合，
     * prefix是指注解在Controller上的@RequestMapping中的请求路径。
     *
     * @param permissionEntity prefix属性为空的permissionEntity
     */
    private Set<MyGrantedAuthority> getAuthoritiesWithEmptyPrefix(PermissionEntity permissionEntity) {
        Set<MyGrantedAuthority> authorities = new HashSet<>();

        String urlStr = permissionEntity.getUrl();
        String method = permissionEntity.getMethod();
        for (String url : urlStr.split(",")) {
            MyGrantedAuthority myGrantedAuthority = new MyGrantedAuthority(url, method);
            authorities.add(myGrantedAuthority);
        }
        return authorities;
    }
}
