package com.example.cloudstore.service.impl;

import com.example.cloudstore.controller.CodeController;
import com.example.cloudstore.validate.SmsCode;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class SmsService {
    private String apikey="45e68c5f9dc6830c8901cc3f9a54291d";
    //腾讯云
    // 短信应用SDK AppID
    private Integer appid = 1400086843;
    // 短信应用SDK AppKey
    private String appkey = "d6489afb7ac87a25d1d0c7aec997282a";
    // 短信模板ID，需要在短信应用中申请
    private Integer templateId =112087;
    // 签名——另外签名参数使用的是`签名内容`，而不是`签名ID`
    private String smsSign = "猿来极客";
    public void sendMessage(String mobile ,String yzm) throws HTTPException {
        //腾讯云
        String[] params = {yzm,"10"};
        sendSms(appid,appkey,mobile,templateId,smsSign,params);
    }
    public  void sendSms(int appid,String appkey,String phoneNumbers,int templateId,String smsSign,String[] params) throws HTTPException {
        try {
            SmsSingleSender ssender = new SmsSingleSender(appid,appkey);
            SmsSingleSenderResult result;
            result = ssender.sendWithParam("86", phoneNumbers, templateId, params, smsSign, "", "");
            System.out.print(result);
        } catch (javax.xml.ws.http.HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
    }

    /**
     * @Author: jitdc
     * @Date: 16:36 2018/6/29
     * @Description: 验证验证码是否正确
     */
    public boolean checkSmsCode(HttpServletRequest request, String sms){
        HttpSession session = request.getSession();
        SmsCode smsCode = (SmsCode)session.getAttribute(CodeController.SESSION_KEY_SMS);
//        System.out.println("11111111" + smsCode.getCode());
//        System.out.println("22222" + sms);
        if (smsCode.getCode().equals(sms)){
            return true;
        }
        else
            return false;
    }
}
