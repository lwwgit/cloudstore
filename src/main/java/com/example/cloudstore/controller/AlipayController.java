package com.example.cloudstore.controller;

import com.alipay.api.AlipayApiException;
import com.example.cloudstore.repository.UserInfoRepository;
import com.example.cloudstore.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;

@RestController
public class AlipayController {

    @Value("${WEB_IP}")
    private String WEB_IP;

    @Autowired
    AlipayService alipayService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @GetMapping("/become/vip")
    public String PayVip() throws UnsupportedEncodingException, AlipayApiException {
        String result = alipayService.PayVip();
        System.out.println("Pay Return: " + result);
        return result;
    }

    @GetMapping("/return_url")
    public View ReturnUrl() throws UnsupportedEncodingException, AlipayApiException {
        System.out.println("Already in return_url");
        alipayService.Notify();
        return new RedirectView(WEB_IP + "/#/home/all");

    }

    @PostMapping("/notify_url")
    public void NotifyUrl() throws UnsupportedEncodingException, AlipayApiException {
        System.out.println("Already in notify_url");
//        alipayService.Notify();
    }
}