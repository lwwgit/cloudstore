package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole,Integer> {

    List<SysUserRole> findByUserId(Integer id);
}
