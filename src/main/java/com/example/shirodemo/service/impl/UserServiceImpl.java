package com.example.shirodemo.service.impl;

import com.example.shirodemo.dao.UserRepository;
import com.example.shirodemo.entity.User;
import com.example.shirodemo.service.UserService;
import com.example.shirodemo.util.SaltUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public User findByUsername(String username) {
        if (username != null) {
            return userRepository.findByUsername(username);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User register(User user) {
        User userExisted = userRepository.findByUsername(user.getUsername());
        if (userExisted == null) {
            user.setSalt(SaltUtil.getSalt());
            user.setPassword(new Md5Hash(user.getPassword(), user.getSalt(), 1024).toHex());
            return userRepository.save(user);
        }
        return null;
    }
}
