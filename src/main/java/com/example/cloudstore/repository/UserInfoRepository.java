package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {

     UserInfo findByUsername(String username);
}
