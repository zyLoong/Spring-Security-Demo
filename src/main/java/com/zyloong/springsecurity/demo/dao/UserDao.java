package com.zyloong.springsecurity.demo.dao;

import com.zyloong.springsecurity.demo.entity.UserEntity;

/**
 * @author zyloong
 */
public interface UserDao {
    UserEntity getUserByName(String username);
}
