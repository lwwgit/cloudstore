package com.example.cloudstore.authentication;

import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.domain.entity.UserInfo;
import com.example.cloudstore.service.SysUserService;
import com.example.cloudstore.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserInfoService userInfoService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功");
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(authentication));
        String token = Jwts.builder()
                .setSubject(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                .signWith(SignatureAlgorithm.HS512, "MyJwtSecret")
                .compact();
//        response.addHeader("Authentication", token);
        String username = ((User) authentication.getPrincipal()).getUsername();
        SysUser sysUser = sysUserService.selectByName(username);
        UserInfo userInfo = userInfoService.findByUsername(username);
        Boolean isVIP = false;
        if (userInfo.getVip().equals(1)){
            isVIP = true;
        }
        String tel = sysUser.getTel();
        Integer state = sysUser.getState();
        Integer com = sysUser.getCom();
        Collection<GrantedAuthority> role = ((User) authentication.getPrincipal()).getAuthorities();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println("{\"code\":0,\"msg\":\"登录成功\",\"Token\":\""+ token + "\",\"username\":\""+ username + "\",\"role\":\""+ role + "\",\"tel\":\""+ tel + "\",\"state\":\""+ state + "\",\"com\":\""+ com + "\",\"isVIP\":\""+ isVIP + "\"}");
    }


}
