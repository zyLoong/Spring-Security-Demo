package com.zyloong.springsecurity.demo.dao;

import com.zyloong.springsecurity.demo.entity.PermissionEntity;

import java.util.Set;

/**
 * @author zyloong
 */
public interface PermissionDao {
    Set<PermissionEntity> listByUserId(Integer id);
}
