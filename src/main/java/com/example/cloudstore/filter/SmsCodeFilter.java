package com.example.cloudstore.filter;


import com.example.cloudstore.authentication.MyAuthenticationFailureHandler;
import com.example.cloudstore.exception.ValidateCodeException;
import com.example.cloudstore.validate.SmsCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SmsCodeFilter extends OncePerRequestFilter {
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/authentication/mobile",httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(),"post")){
            try {
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
        SmsCode codeInSession =(SmsCode)session.getAttribute("SESSION_KEY_SMS_CODE");
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),"smsCode");
        if (StringUtils.isBlank(codeInRequest)){
            throw new ValidateCodeException("短信验证码不能为空");
        }
        if (codeInSession == null){
            throw new ValidateCodeException("短信验证码不存在");
        }
        if (codeInSession.isExpired()){
            session.removeAttribute("SESSION_KEY_SMS_CODE");
            throw new ValidateCodeException("短信验证码已过期");
        }
        if (!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
            throw  new ValidateCodeException("短信验证码错误");
        }
        session.removeAttribute("SESSION_KEY_SMS_CODE");
    }

    public MyAuthenticationFailureHandler getMyAuthenticationFailureHandler() {
        return myAuthenticationFailureHandler;
    }

    public void setMyAuthenticationFailureHandler(MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
    }
}
