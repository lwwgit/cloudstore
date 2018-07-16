package com.example.cloudstore.controller;

import com.example.cloudstore.domain.PrivateSpace;
import com.example.cloudstore.domain.Result;
import com.example.cloudstore.repository.PrivateSpaceRepository;
import com.example.cloudstore.service.PrivateSpaceService;
import com.example.cloudstore.utils.ResultUtil;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author jitdc
 * @Date Create in 14:27 2018/7/13
 * @Description: 私人空间
 */
@RestController
@RequestMapping(value = "/privateSpace")
public class PrivateSpaceController {
    @Autowired
    private PrivateSpaceService privateSpaceService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * @Author: jitdc
     * @Date: 15:16 2018/7/13
     * @Description: 设置私人空间密码
     */
    @PostMapping("/setPassword")
    public Result setPrivateSpace(PrivateSpace privateSpace){
        privateSpace.setPrivatePassword(passwordEncoder.encode(privateSpace.getPrivatePassword()));
        PrivateSpace result = privateSpaceService.insert(privateSpace);
        if (result == null)
            return ResultUtil.error(100,"设置私人密码出错");
        else
            return ResultUtil.success(result);
    }
    /**
     * @Author: jitdc
     * @Date: 15:33 2018/7/13
     * @Description: 检查私人空间密码是否正确
     */
    @PostMapping("/checkPassword")
    public Result checkPrivatePassword(String username,String password){
        PrivateSpace byUsername = privateSpaceService.findByUsername(username);
        boolean matches = passwordEncoder.matches(password, byUsername.getPrivatePassword());
        if (matches)
            return ResultUtil.success("密码正确");
        else
            return ResultUtil.error(100,"密码错误");
    }
}
