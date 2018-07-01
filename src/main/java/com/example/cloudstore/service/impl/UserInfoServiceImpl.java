package com.example.cloudstore.service.impl;


import com.example.cloudstore.domain.entity.UserInfo;
import com.example.cloudstore.repository.UserInfoRepository;
import com.example.cloudstore.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author jitdc
 * @Date Create in 15:20 2018/6/29
 * @Description: 用户信息表
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository repository;

    @Override
    public UserInfo insert(UserInfo userInfo) {
        return repository.save(userInfo);
    }

    @Override
    public UserInfo findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        return repository.save(userInfo);
    }
}
