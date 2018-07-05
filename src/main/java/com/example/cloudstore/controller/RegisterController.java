package com.example.cloudstore.controller;


import com.example.cloudstore.domain.Result;
import com.example.cloudstore.domain.entity.*;
import com.example.cloudstore.service.*;
import com.example.cloudstore.service.impl.SmsService;
import com.example.cloudstore.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * @Author jitdc
 * @Date Create in 13:59 2018/6/29
 * @Description: 用户注册
 */

@RestController
public class RegisterController {

    @Autowired
    private SysUserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserStoreService userStoreService;

    @Autowired
    private IconService iconService;


    /**
     * @Author: jitdc
     * @Date: 14:12 2018/6/29
     * @Description: 判断用户名和手机号是否已存在
     */

    @RequestMapping(value = "/loginPage")
    public Result loginPage(){
        return ResultUtil.error(100, "请先登录");
    }

    @GetMapping("/user/check")
    public Result checkUser(String username,String tel){
        if (userService.selectByName(username) != null) {
            return ResultUtil.error(1, "用户名已存在");
        }
        if (userService.selectByTel(tel) != null) {
            return ResultUtil.error(1, "手机号已被占用");
        }
        return ResultUtil.success();
    }

    @PostMapping("/register")
    public Result register(HttpServletRequest request,
                           SysUser sysUser,
                           String sms) throws IOException {
        System.out.println("44444444" + sms);

        boolean result = smsService.checkSmsCode(request,sms);
        if (result == true) {
            if (userService.selectByName(sysUser.getUsername()) != null) {
                return ResultUtil.error(1, "用户名已存在");
            }
            if (userService.selectByTel(sysUser.getTel()) != null) {
                return ResultUtil.error(1, "手机号已被占用");
            }
            SysUser user = new SysUser();
            user.setUsername(sysUser.getUsername());
            user.setCreateDate(new Date());
            user.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            user.setTel(sysUser.getTel());
            userService.insert(user);

            SysRole sysRole = sysRoleService.findByRole("ROLE_USER");
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(sysRole.getId());
            sysUserRole.setUserId(user.getId());
            userRoleService.insert(sysUserRole);

            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfoService.insert(userInfo);

            UserStore userStore = new UserStore();
            userStore.setUsername(user.getUsername());
            userStore.setDir(user.getUsername());
            userStore.setAvailableCapacity("2GB");
            userStoreService.insert(userStore);

            //在hadoop上创建属于用户的根文件夹并在创建回收站目录
            String username = user.getUsername();
            String userDirPath = "/" + username;
            boolean result1 =  iconService.createDir(userDirPath);
            if(result1 == true){
                String userTmpPath = userDirPath + "tmp";
                boolean result2 =  iconService.createDir(userTmpPath);
                if(result2 == true){
                    return ResultUtil.success();
                }
            }
            return ResultUtil.error(2,"失败");

        }else {
            // 验证码超时或输入错误
            return ResultUtil.error(1, "验证码错误");
        }
    }
}
