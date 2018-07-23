package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole,Integer> {

    List<SysUserRole> findByUserId(Integer id);

    List<SysUserRole> findByRoleId(Integer id);

    @Query("select count(s) from SysUserRole s where s.roleId=1")
    Integer countAdmin();
}
