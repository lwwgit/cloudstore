package com.example.cloudstore.service.impl;

import com.example.cloudstore.domain.Md5;
import com.example.cloudstore.repository.Md5Repository;
import com.example.cloudstore.service.Md5service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author jitdc
 * @Date Create in 16:35 2018/7/12
 * @Description:
 */
@Service
public class Md5serviceImpl implements Md5service {
    @Autowired
    private Md5Repository md5Repository;

    @Override
    public Md5 save(Md5 md5) {
        return md5Repository.save(md5);
    }
}
