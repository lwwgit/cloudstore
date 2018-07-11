package com.example.cloudstore.controller;

import com.alipay.api.AlipayApiException;
import com.example.cloudstore.repository.UserInfoRepository;
import com.example.cloudstore.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
public class AlipayController {

    @Autowired
    AlipayService alipayService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @GetMapping(value = "/become/vip")
    public String PayVip() throws UnsupportedEncodingException, AlipayApiException {
        return alipayService.PayVip();
    }

    @GetMapping("/return_url")
    public void ReturnUrl() throws UnsupportedEncodingException, AlipayApiException {
        alipayService.Notify();
        /*
        *
        * 这里调用alipayService.Notify()对数据库进行操作
        * 应该在异步通知notify_url里进行调用
        * 但异步通知需要公网ip
        * 宝宝的本机不是公网
        * 很无奈啊
        * 哭~~~
        *
        * */

    }

    @PostMapping("/notify_url")
    public void NotifyUrl() throws UnsupportedEncodingException, AlipayApiException {
        System.out.println("###### In notify_url");
//        alipayService.Notify();
    }
}