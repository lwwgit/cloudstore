package com.example.cloudstore.controller;

import com.example.cloudstore.domain.Result;
import com.example.cloudstore.service.impl.SmsService;
import com.example.cloudstore.utils.RandomValidateCodeUtil;
import com.example.cloudstore.utils.ResultUtil;
import com.example.cloudstore.validate.ImageCode;
import com.example.cloudstore.validate.SmsCode;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class CodeController {
    @Autowired
    private SmsService smsService;

    /**
     * 获取图片验证码
     * 获取手机号验证码
     */

    public static final String SESSION_KEY_IMAGE = "SESSION_KEY_IMAGE_CODE";
    public static final String SESSION_KEY_SMS = "SESSION_KEY_SMS_CODE";
    /**
     * @Author: jitdc
     * @Date: 16:42 2018/6/29
     * @Description: 图片验证码接口
     */
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
        ImageCode imageCode = randomValidateCode.createImageCode(request);
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_KEY_IMAGE,imageCode);
        //获取session的Id
        String sessionId = session.getId();
        //判断session是不是新创建的
        if (session.isNew()) {
            System.out.println("生成验证码时session创建成功，session的id是："+sessionId);
        }else {
            System.out.println("生成验证码时服务器已经存在该session了，session的id是："+sessionId);
        }
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
    }

    /**
     * @Author: jitdc
     * @Date: 16:42 2018/6/29
     * @Description: 手机验证码接口
     */
    @GetMapping("/code/sms")
    public String createSmsCode(HttpServletRequest request,@RequestParam("mobile")String mobile) throws IOException, ServletRequestBindingException, HTTPException {
        RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
        SmsCode smsCode1 = randomValidateCode.createSmsCode(request);
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_KEY_SMS,smsCode1);
//        smsService.sendMessage(mobile,smsCode1.getCode());
        System.out.println("手机验证码是："+smsCode1.getCode());
        String res ="{\"code\":200,\"message\":\"发送成功\"}";
        return res;
    }

    /**
     * @Author: jitdc
     * @Date: 16:36 2018/6/29
     * @Description: 验证验证码是否正确
     */
    @GetMapping("/code/sms/verify")
    public Result checkSmsCode(HttpServletRequest request, @RequestParam("sms")String sms){
        HttpSession session = request.getSession();
        SmsCode smsCode = (SmsCode)session.getAttribute(SESSION_KEY_SMS);
        if (smsCode.getCode().equals(sms)){
            return ResultUtil.success();
        }
        else
            return ResultUtil.error(1,"短信验证码错误");
    }

}
