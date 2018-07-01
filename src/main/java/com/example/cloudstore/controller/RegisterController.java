package com.example.cloudstore.controller;


import com.example.cloudstore.domain.Result;
import com.example.cloudstore.domain.entity.*;
import com.example.cloudstore.service.*;
import com.example.cloudstore.service.impl.SmsService;
import com.example.cloudstore.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author jitdc
 * @Date Create in 13:59 2018/6/29
 * @Description: 用户注册
 */
@CrossOrigin
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
    /**
     * @Author: jitdc
     * @Date: 14:12 2018/6/29
     * @Description: 判断用户名和手机号是否已存在
     */

    @PostMapping("/register")
    public Result register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("tel")String tel,
                           @RequestParam("sms") String sms,
                           HttpServletRequest request){

        boolean result = smsService.checkSmsCode(request,sms);
        if (result == true) {
            if (userService.selectByName(username) != null) {
                return ResultUtil.error(1, "用户名已存在");
            }
            if (userService.selectByTel(tel) != null) {
                return ResultUtil.error(1, "手机号已被占用");
            }
            SysUser user = new SysUser();
            user.setUsername(username);
            user.setCreateDate(new Date());
            user.setPassword(passwordEncoder.encode(password));
            user.setTel(tel);
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

            return ResultUtil.success();
        }else {
            // 验证码超时或输入错误
            return ResultUtil.error(1, "验证码错误");
        }
    }
}
