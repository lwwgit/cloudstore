package com.example.cloudstore.service;

import com.example.cloudstore.domain.PrivateSpace;

/**
 * @Author jitdc
 * @Date Create in 14:28 2018/7/13
 * @Description: 私人空间接口
 */
public interface PrivateSpaceService {
    PrivateSpace insert(PrivateSpace privateSpace);
    PrivateSpace findByUsername(String username);
}
