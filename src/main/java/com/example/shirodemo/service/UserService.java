package com.example.shirodemo.service;

import com.example.shirodemo.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户
     */
    User findByUsername(String username);

    /**
     * 查找所有用户
     * @return 所有用户
     */
    List<User> findAll();

    /**
     * 注册用户
     * @param user 用户
     * @return 用户
     */
    User register(User user);
}
