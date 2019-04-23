package com.zyloong.springsecurity.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zyloong
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    // 用户id
    private Integer id;
    // 用户名
    private String username;
    // 用户创建时间
    private String createtime;
    // 用户角色ID列表
    private List<Integer> roleIds;
    // 用户密码
    private String password;
    //    用户状态：true 表示可用, false 表示被锁定，禁止登录
    //    该字段对应org.springframework.security.core.userdetails.User中的accountNonLocked字段
    private boolean status;
}