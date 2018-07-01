package com.example.cloudstore.service.impl;

import com.example.cloudstore.domain.entity.UserStore;
import com.example.cloudstore.repository.UserStoreRepository;
import com.example.cloudstore.service.UserStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStoreServiceImpl implements UserStoreService{
    @Autowired
    UserStoreRepository userStoreRepository;
    @Override
    public UserStore insert(UserStore userStore) {
        return userStoreRepository.save(userStore);
    }

    @Override
    public UserStore update(UserStore userStore) {
        return userStoreRepository.save(userStore);
    }

    @Override
    public UserStore findByUsername(String username) {
        return userStoreRepository.findByUsername(username);
    }
}
