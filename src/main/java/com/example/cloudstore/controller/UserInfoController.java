package com.example.cloudstore.controller;

import com.example.cloudstore.domain.Result;
import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.domain.entity.UserInfo;
import com.example.cloudstore.service.SysUserService;
import com.example.cloudstore.service.UserInfoService;
import com.example.cloudstore.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SysUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GlobalFunction globalFunction;

    /**
     * 忘记密码
     */
    @PostMapping("/forget/password")
    public Result  forgetPassword(String newPassword,String tel){
         SysUser sysUser = userService.selectByTel(tel);
         if(sysUser == null){
             return ResultUtil.error(1,"用户不存在");
         }
         sysUser.setPassword(passwordEncoder.encode(newPassword));
         userService.update(sysUser);
         return ResultUtil.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/change/password")
    public Result  changePassword(String newPassword){
        String currentUsername = globalFunction.getUsername();
        SysUser sysUser = userService.selectByName(currentUsername);
        sysUser.setPassword(passwordEncoder.encode(newPassword));
        userService.update(sysUser);
        return ResultUtil.success();
    }

    /**
     * 修改手机号
     */
    @PutMapping("/change/tel")
    public Result  changeTel(String newTel){
        if(userService.selectByTel(newTel) != null){
            return ResultUtil.error(1,"手机号已被注册");
        }
        String currentUsername = globalFunction.getUsername();
        SysUser sysUser = userService.selectByName(currentUsername);
        sysUser.setTel(newTel);
        userService.update(sysUser);
        return ResultUtil.success();
    }

    /**
     * 添加（修改）个人信息
     */
    @PostMapping("/user/info")
    public Result userInfo( UserInfo userInfo){
        String currentUsername = globalFunction.getUsername();
        userInfo.setUsername(currentUsername);
        userInfoService.update(userInfo);
        return ResultUtil.success();
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/get/user/info")
    public Result getUserInfo(){
        String currentUsername = globalFunction.getUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        return ResultUtil.success(userInfo);
    }

    /**
     * 上传头像
     */


    /**
     * 获取头像
     */
}
