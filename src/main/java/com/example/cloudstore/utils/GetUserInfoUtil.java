package com.example.cloudstore.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author jitdc
 * @Date Create in 12:06 2018/7/19
 * @Description:
 */
public class GetUserInfoUtil {
    /**
     * 获取当前登录用户的用户名
     *
     * @return
     */
    public String getUsername() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
//        String username = userDetails.getUsername();
//        return username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }
}
