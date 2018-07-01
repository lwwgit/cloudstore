package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserRepository extends JpaRepository<SysUser,Integer> {

    SysUser findByUsername(String username);

    SysUser findByTel(String tel);
}
