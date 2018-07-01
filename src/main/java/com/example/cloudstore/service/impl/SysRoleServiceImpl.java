package com.example.cloudstore.service.impl;


import com.example.cloudstore.domain.entity.SysRole;
import com.example.cloudstore.repository.SysRoleRepository;
import com.example.cloudstore.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    SysRoleRepository repository;

    @Override
    public SysRole SelectById(Integer id) {
        return repository.findSysRoleById(id);
    }

    @Override
    public SysRole insert(SysRole sysRole) {
        return repository.save(sysRole);
    }

    @Override
    public SysRole findByRole(String role) {
        return repository.findByName(role);
    }
}
