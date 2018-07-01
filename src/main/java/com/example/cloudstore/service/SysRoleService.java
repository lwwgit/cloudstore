package com.example.cloudstore.service;

import com.example.cloudstore.domain.entity.SysRole;

/**
 * @Author jitdc
 * @Date Create in 15:32 2018/6/29
 * @Description: 角色表
 */
public interface SysRoleService {

    /*
     * @Description: 根据id读取角色类
     */
    SysRole SelectById(Integer id);

    SysRole insert(SysRole sysRole);

    SysRole findByRole(String role);
}
