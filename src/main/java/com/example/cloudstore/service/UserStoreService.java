package com.example.cloudstore.service;

import com.example.cloudstore.domain.entity.UserStore;

public interface UserStoreService {
    UserStore insert (UserStore userStore);

    UserStore update(UserStore userStore);

    UserStore findByUsername(String username);
}
