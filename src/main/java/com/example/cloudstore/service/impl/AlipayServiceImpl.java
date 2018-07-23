package com.example.cloudstore.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.cloudstore.authentication.provider.AlipayConfig;
import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.entity.OrderInfo;
import com.example.cloudstore.domain.entity.UserInfo;
import com.example.cloudstore.domain.entity.UserStore;
import com.example.cloudstore.repository.OrderInfoRepository;
import com.example.cloudstore.repository.UserInfoRepository;
import com.example.cloudstore.repository.UserStoreRepository;
import com.example.cloudstore.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    OrderInfoRepository orderInfoRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserStoreRepository userStoreRepository;

    @Autowired
    HttpServletRequest request;

    @Override
    public String PayVip() throws UnsupportedEncodingException, AlipayApiException {
        GlobalFunction globalFunction = new GlobalFunction();
        String username = globalFunction.getUsername();
//        String username = "shw";
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        if (userInfo.getVip().equals("1")) {
            return "Already Vip";
        }
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = formatter.format(new Date());

        //订单Id，格式username&&nowTime
        String out_trade_no = "UserId=" + userInfo.getId()+ "  TradeTime=" + nowTime;
        //订单Name，格式username&&nowTime&&month
        String subject = "User=" + userInfo.getId() + "  TradeTime=" + nowTime;
        //订单金额，格式month*10
        String total_amount = "200";

//        //订单号，必填
//        String out_trade_no = new String(orderId.getBytes("ISO-8859-1"), "UTF-8");
//        //付款金额，必填
//        String total_amount = new String(totalMoney.getBytes("ISO-8859-1"), "UTF-8");
//        //订单名称，必填
//        String subject = new String(orderName.getBytes("ISO-8859-1"), "UTF-8");

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }

    @Override
    public void Notify() throws UnsupportedEncodingException, AlipayApiException {
        GlobalFunction globalFunction = new GlobalFunction();
        String username = globalFunction.getUsername();
//        String username = "shw";
//获取支付宝返回过来的信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        System.out.println("params: " + params + "\n requestParams: " + requestParams);
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key,
                AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        if (signVerified) {
            //添加订单信息
            OrderInfo orderInfo = new OrderInfo();
            String alipayNo = request.getParameter("out_trade_no");
            String money = request.getParameter("total_amount");

            System.out.println("alipayNo: " + alipayNo + "money: " + money);

            orderInfo.setAlipayNo(alipayNo);
            orderInfo.setMoney(money);
            orderInfoRepository.save(orderInfo);
            System.out.println("订单信息打印：" + orderInfo);

            //修改用户信息为vip
            UserInfo userInfo = userInfoRepository.findByUsername(username);
            userInfo.setVip("1");
            userInfoRepository.save(userInfo);

            UserStore userStore = userStoreRepository.findByUsername(username);
            userStore.setAvailableCapacity("5GB");
            userStoreRepository.save(userStore);
        } else {
            System.out.println("验签未通过");
            System.out.println("验签为： " + signVerified);
        }
    }
}
