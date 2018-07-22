package com.example.cloudstore.repository;

import com.example.cloudstore.domain.JsonUser2Adm;
import com.example.cloudstore.domain.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysUserRepository extends JpaRepository<SysUser,Integer> {

    SysUser findByUsername(String username);

    SysUser findByTel(String tel);

    @Override
    List<SysUser> findAll();//查询所有用户


    Page<SysUser> findAll (Pageable pageable);

    @Modifying
    @Transactional
    @Query("Update SysUser  s Set s.state = 1 where s.username = ?1")
    int modeEnable(  String username);

    @Modifying
    @Transactional
    @Query("Update SysUser  s Set s.state = 0 where s.username = ?1")
    int modeFreeze(String username);

    @Modifying
    @Transactional
    @Query("Update SysUser  s Set s.com = 1 where s.username = ?1")
    int comSub(String username);

    @Modifying
    @Transactional
    @Query("Update SysUser  s Set s.com = 0 where s.username = ?1")
    int comCan(String username);
}
