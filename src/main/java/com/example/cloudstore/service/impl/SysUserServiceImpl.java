package com.example.cloudstore.service.impl;


import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.repository.SysUserRepository;
import com.example.cloudstore.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository repository;

    @Override
    public SysUser insert(SysUser user) {
        return repository.save(user);
    }

    @Override
    public SysUser selectByName(String name) {
        return repository.findByUsername(name);
    }

    @Override
    public SysUser selectByTel(String tel) {
        return repository.findByTel(tel);
    }

    @Override
    public SysUser update(SysUser user) {
        return repository.save(user);
    }
}
