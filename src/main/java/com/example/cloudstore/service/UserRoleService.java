package com.example.cloudstore.service;


import com.example.cloudstore.domain.entity.SysUserRole;

import java.util.List;

/**
 * @Author jitdc
 * @Date Create in 15:53 2018/6/29
 * @Description: 用户角色关系表
 */
public interface UserRoleService {
    /*
     * @Description: 根据用户id得到用户有哪些角色
     */
    List<SysUserRole> selectByUserId(Integer id);

    SysUserRole insert(SysUserRole sysUserRole);
}
