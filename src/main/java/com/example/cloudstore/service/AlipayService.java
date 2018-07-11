package com.example.cloudstore.service;

import com.alipay.api.AlipayApiException;

import java.io.UnsupportedEncodingException;

public interface AlipayService {
    String PayVip() throws UnsupportedEncodingException, AlipayApiException;

    void Notify() throws UnsupportedEncodingException, AlipayApiException;
}
