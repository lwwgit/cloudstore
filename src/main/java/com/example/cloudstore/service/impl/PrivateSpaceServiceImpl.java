package com.example.cloudstore.service.impl;


import com.example.cloudstore.domain.PrivateSpace;
import com.example.cloudstore.domain.Result;
import com.example.cloudstore.repository.PrivateSpaceRepository;
import com.example.cloudstore.service.PrivateSpaceService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Author jitdc
 * @Date Create in 14:28 2018/7/13
 * @Description:  私人空间接口的实现类
 */
@Service
public class PrivateSpaceServiceImpl implements PrivateSpaceService {
    @Autowired
    private PrivateSpaceRepository privateSpaceRepository;

    @Override
    public PrivateSpace insert(PrivateSpace privateSpace) {
        PrivateSpace save = privateSpaceRepository.save(privateSpace);
        return save;
    }

    @Override
    public PrivateSpace findByUsername(String username) {
        PrivateSpace result = privateSpaceRepository.findByUsername(username);
        return result;
    }
}
