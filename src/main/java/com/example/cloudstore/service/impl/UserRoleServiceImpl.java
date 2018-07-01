package com.example.cloudstore.service.impl;


import com.example.cloudstore.domain.entity.SysUserRole;
import com.example.cloudstore.repository.SysUserRoleRepository;
import com.example.cloudstore.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private SysUserRoleRepository repository;

    @Override
    public List<SysUserRole> selectByUserId(Integer id) {
        return repository.findByUserId(id);
    }

    @Override
    public SysUserRole insert(SysUserRole sysUserRole) {
        return repository.save(sysUserRole);
    }
}
