package com.example.cloudstore.filter;


import com.example.cloudstore.authentication.MyAuthenticationFailureHandler;
import com.example.cloudstore.exception.ValidateCodeException;
import com.example.cloudstore.validate.ImageCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ValidateCodeFilter extends OncePerRequestFilter {
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/authentication/login",httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(),"post")){
            try {
//                String codeInRequest = httpServletRequest.getParameter("imageCode");
//                System.out.println(codeInRequest);
                validate(new ServletWebRequest(httpServletRequest),httpServletRequest);
            }catch (ValidateCodeException e){
                myAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(ServletWebRequest servletWebRequest, HttpServletRequest request) throws ServletRequestBindingException {
        HttpSession session = request.getSession();
        //获取session的Id
        String sessionId = session.getId();
        //判断session是不是新创建的
        if (session.isNew()) {
            System.out.println("校验验证码时session创建成功，session的id是："+sessionId);
        }else {
            System.out.println("校验验证码时服务器已经存在该session了，session的id是："+sessionId);
        }
        ImageCode codeInSession =(ImageCode)session.getAttribute("SESSION_KEY_IMAGE_CODE");
        String codeInRequest = request.getParameter("imageCode");
        // String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),"imageCode");
//        System.out.println("session中的验证码是："+codeInSession.getCode()+"   用户输入的验证码是："+codeInRequest);
        if (StringUtils.isBlank(codeInRequest)){
            throw new ValidateCodeException("验证码不能为空");
        }
        if (codeInSession == null){
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpired()){
            session.removeAttribute("SESSION_KEY_IMAGE_CODE");
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
            throw  new ValidateCodeException("验证码错误");
        }
        session.removeAttribute("SESSION_KEY_IMAGE_CODE");
    }

    public MyAuthenticationFailureHandler getMyAuthenticationFailureHandler() {
        return myAuthenticationFailureHandler;
    }

    public void setMyAuthenticationFailureHandler(MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
    }
}
