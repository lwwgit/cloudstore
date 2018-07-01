package com.example.cloudstore.security;


import com.example.cloudstore.domain.entity.SysRole;
import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.domain.entity.SysUserRole;
import com.example.cloudstore.service.SysRoleService;
import com.example.cloudstore.service.SysUserService;
import com.example.cloudstore.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    private SysUser sysUser=null;

    @Autowired
    private SysUserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private SysRoleService roleService;

    //处理用户信息获取逻辑
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        sysUser = userService.selectByName(s);
        if (sysUser == null){
            sysUser = userService.selectByTel(s);
            if (sysUser == null)
                throw new UsernameNotFoundException("用户名不存在");
        }


        // 添加权限
        //根据用户id查看用户有哪些角色
        List<SysUserRole> userRoles = userRoleService.selectByUserId(sysUser.getId());

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (SysUserRole userRole : userRoles) {
            //userRole.getRole_id():获取角色的id
            //roleService.SelectById(userRole.getRole_id()):根据角色id获取角色
//            System.out.println("ROLEID   " +userRole.getId() );
            SysRole role = roleService.SelectById(userRole.getRoleId());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        // 返回UserDetails实现类
        //四个true可以判断出 账户可用、账户没有过期、密码没有过期、账户没有被冻结
        return new User(sysUser.getUsername(),sysUser.getPassword(),true,true,true,true,
                grantedAuthorities);
    }

}
