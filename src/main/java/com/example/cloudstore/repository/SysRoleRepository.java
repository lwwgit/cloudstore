package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysRoleRepository extends JpaRepository<SysRole,Integer> {

    SysRole findByName(String name);

    SysRole findSysRoleById(Integer id);
}
