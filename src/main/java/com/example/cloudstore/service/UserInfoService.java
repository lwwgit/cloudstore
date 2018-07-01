package com.example.cloudstore.service;


import com.example.cloudstore.domain.entity.UserInfo;

/**
 * @Author jitdc
 * @Date Create in 15:33 2018/6/29
 * @Description: 用户信息表
 */
public interface UserInfoService {

    /*
     * @Description: 添加一条数据
     */
    UserInfo insert(UserInfo userInfo);

    /*
     * @Description: 通过用户名查找用户信息
     */
    UserInfo findByUsername(String username);

    /*
     * @Description: 更新一条数据
     */
    UserInfo update(UserInfo userInfo);
}
