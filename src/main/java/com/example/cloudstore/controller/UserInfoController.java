package com.example.cloudstore.controller;

import com.example.cloudstore.domain.Result;
import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.service.SysUserService;
import com.example.cloudstore.service.UserInfoService;
import com.example.cloudstore.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 修改密码
     */
    @PostMapping("/change/password")
    public Result  changePassword(@RequestParam("password") String newPassword,
                                  @RequestParam("tel") String tel){

         SysUser sysUser = userService.selectByTel(tel);
         if(sysUser == null){
             return ResultUtil.error(1,"用户不存在");
         }
         sysUser.setPassword(passwordEncoder.encode(newPassword));
         userService.update(sysUser);
         return ResultUtil.success();
    }

    /**
     * 获取原有的手机号
     */
    @GetMapping("/get/tel")
    public Result getUserTel(){
        String currentUsername = globalFunction.getUsername();
        SysUser sysUser = userService.selectByName(currentUsername);
        return ResultUtil.success(sysUser.getTel());
    }

    /**
     * 修改手机号
     */
    @PostMapping("/change/tel")
    public Result  changeTel(@RequestParam("tel") String newTel){
        if(userService.selectByTel(newTel) != null){
            return ResultUtil.error(1,"用户已存在");
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


    /**
     * 获取个人信息
     */

    /**
     * 上传头像
     */


    /**
     * 获取头像
     */
}
