package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoreRepository extends JpaRepository<UserStore,Integer>{

    UserStore findByUsername(String username);
}
