package com.example.cloudstore.service;


import com.example.cloudstore.domain.entity.SysUser;

/**
 * @Author jitdc
 * @Date Create in 15:45 2018/6/29
 * @Description:
 */
public interface SysUserService {
    /*
     * @Description: 添加一条用户数据
     */
    SysUser insert(SysUser user);

    /*
     * @Description: 根据用户名获取一条用户数据
     */
    SysUser selectByName(String name);

    /*
     * @Description: 根据手机号获取一条用户数据
     */
    SysUser selectByTel(String tel);

    /*
     * @Description: 修改SysUser的数据
     */
    SysUser update(SysUser user);
}
