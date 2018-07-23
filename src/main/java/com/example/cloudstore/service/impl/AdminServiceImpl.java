package com.example.cloudstore.service.impl;


import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.*;
import com.example.cloudstore.domain.entity.Complaint;
import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.domain.entity.UserInfo;
import com.example.cloudstore.repository.ComplaintRepository;
import com.example.cloudstore.repository.SysUserRepository;
import com.example.cloudstore.repository.UserInfoRepository;
import com.example.cloudstore.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Override
    public Page<SysUser> UserDisplay(Pageable pageable) {
       return sysUserRepository.findAll(pageable);
    }

    @Override
    public UserInfo userInfoDisplay(String username) {
        return userInfoRepository.findByUsername(username);
    }

    @Override
    public List<JsonUser2Adm> vipInfoDisPlay() {
        List<UserInfo> userInfos = userInfoRepository.userInfoSelectVip();
        List<JsonUser2Adm> jsonUser2Adms = new ArrayList<>();
        for (int i=0;i<userInfos.size();i++){
            JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
            String username = userInfos.get(i).getUsername();
            SysUser user = sysUserRepository.findByUsername(username);
            Date date = user.getCreateDate();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
            jsonUser2Adm.setCreate_date(dateStr);
            jsonUser2Adm.setId(userInfos.get(i).getId());
            jsonUser2Adm.setVip("vip用户");
            jsonUser2Adm.setSex(userInfos.get(i).getSex());
            jsonUser2Adm.setCity(userInfos.get(i).getCity());
            jsonUser2Adm.setUsername(username);
            Integer com = user.getCom();
            if (com.equals(1)){
                jsonUser2Adm.setCom("已申诉");
            }else {
                jsonUser2Adm.setCom("未申诉");
            }
            Integer state = user.getState();
            if (state.equals(1)){
                jsonUser2Adm.setState("正常使用");
            } else {
                jsonUser2Adm.setState("已被冻结");
            }
            jsonUser2Adms.add(jsonUser2Adm);
        }
        return jsonUser2Adms;
    }

    @Override
    public List<JsonUser2Adm> novipInfoDisPlay() {
        List<UserInfo> userInfos = userInfoRepository.userInfoSelectNotVip();
        List<JsonUser2Adm> jsonUser2Adms = new ArrayList<>();
        for (int i=0;i<userInfos.size();i++){
            JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
            String username = userInfos.get(i).getUsername();
            SysUser user = sysUserRepository.findByUsername(username);
            Date date = user.getCreateDate();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
            jsonUser2Adm.setCreate_date(dateStr);
            Integer com = user.getCom();
            Integer state = user.getState();
            if (state.equals(1)){
                jsonUser2Adm.setState("正常使用");
            } else {
                jsonUser2Adm.setState("已被冻结");
            }
            if (com.equals(1)){
                jsonUser2Adm.setCom("已申诉");
            }else {
                jsonUser2Adm.setCom("未申诉");
            }
            jsonUser2Adm.setId(userInfos.get(i).getId());
            jsonUser2Adm.setVip("非vip用户");
            jsonUser2Adm.setSex(userInfos.get(i).getSex());
            jsonUser2Adm.setCity(userInfos.get(i).getCity());
            jsonUser2Adm.setUsername(username);
            jsonUser2Adms.add(jsonUser2Adm);
        }
        return jsonUser2Adms;
    }

    @Override
    public JsonUser2Adm specificDisply(String username) throws URISyntaxException{
        JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
        SysUser sysUser = sysUserRepository.findByUsername(username);
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        jsonUser2Adm.setCity(userInfo.getCity());
        Date date = sysUser.getCreateDate();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
        jsonUser2Adm.setCreate_date(dateStr);
        jsonUser2Adm.setId(userInfo.getId());
        jsonUser2Adm.setSex(userInfo.getSex());
        jsonUser2Adm.setUsername(userInfo.getUsername());
        Integer com = sysUser.getCom();
        if (com.equals(1)){
            jsonUser2Adm.setCom("已申诉");
        }else {
            jsonUser2Adm.setCom("未申诉");
        }
        Integer state = sysUser.getState();
        if (state.equals(1)){
            jsonUser2Adm.setState("正常使用");
        } else {
            jsonUser2Adm.setState("已被冻结");
        }
        GlobalFunction globalFunction = new GlobalFunction();
        try {
            String size = globalFunction.getDirectorySize("/" + username);
            jsonUser2Adm.setUsedsize(size);
            String vip = userInfo.getVip();
            if (vip.equals("1")){
                jsonUser2Adm.setVip("vip用户");
                jsonUser2Adm.setTotalsize("5 GB");
                String Size = size.split(" ")[0];
                float usedsize = Float.parseFloat(Size);
                jsonUser2Adm.setUsedInfo(usedsize/5*1000);
            }else {
                jsonUser2Adm.setVip("普通用户");
                jsonUser2Adm.setTotalsize("2 GB");
                String Size = size.split(" ")[0];
                float usedsize = Float.parseFloat(Size);
                jsonUser2Adm.setUsedInfo(usedsize/2);
            }
        }catch (IOException e){
            e.getMessage();
        }
        return jsonUser2Adm;
    }

    @Override
    public int tobeVip(String username) {
        return userInfoRepository.tobeVip(username);
    }

    @Override
    public int cancleVip(String username) {
        return userInfoRepository.cancleVip(username);
    }

    @Override
    @Transactional
    public int modeEnable(String username) {
        complaintRepository.deleteComplaintByUsername(username);
        sysUserRepository.comCan(username);
        return sysUserRepository.modeEnable(username);
    }
    
    @Override
    public int modeFreeze(String username) {
        return sysUserRepository.modeFreeze(username);
    }

    @Override
    public String comSub(String username, String cominfo) {
        SysUser sysUser = sysUserRepository.findByUsername(username);
        if (sysUser.getState().equals(1)){
            return "账号正常使用，不用申诉";
        }
        Complaint complaint = complaintRepository.findByUsername(username);
        if (complaint != null){
            return "已经申诉过了，请耐心等候";
        }
        Complaint complaint1 = new Complaint();
        complaint1.setCominfo(cominfo);
        complaint1.setUsername(username);
        complaintRepository.save(complaint1);
        sysUserRepository.comSub(username);
        return "申诉成功，请耐心等候";
    }

    @Override
    public Complaint comView(String username) {
        Complaint complaint = complaintRepository.findByUsername(username);
        return complaint;
    }

    @Override
    public Map<String,Object> ageDistribution() {
        Map<String,Object> ageDatas = new HashMap<>();
        ageDatas.put("shisuiyixia",userInfoRepository.shisuiyixia());
        ageDatas.put("shi2shijiu",userInfoRepository.shi2shijiu());
        ageDatas.put("ershi2erjiu",userInfoRepository.ershi2erjiu());
        ageDatas.put("sanshi2sanjiu",userInfoRepository.sanshi2sanjiu());
        ageDatas.put("sishi2sijiu",userInfoRepository.sishi2sijiu());
        ageDatas.put("wushi2wujiu",userInfoRepository.wushi2wujiu());
        ageDatas.put("liushiyishang",userInfoRepository.liushiyishang());

        return ageDatas;
    }

    @Override
    public List<CityData> cityDistribution() {
        List<CityData> cityDatas = new ArrayList<>();
        CityData huadong = new CityData();
        huadong.setArea("华东地区");
        huadong.setNumber(userInfoRepository.HuaDong());
        CityData huabei = new CityData();
        huabei.setArea("华北地区");
        huabei.setNumber(userInfoRepository.HuaBei());
        CityData huanan = new CityData();
        huanan.setArea("华南地区");
        huanan.setNumber(userInfoRepository.HuaNan());
        CityData huazhong = new CityData();
        huazhong.setArea("华中地区");
        huazhong.setNumber(userInfoRepository.HuaZhong());
        CityData xibei = new CityData();
        xibei.setArea("西北地区");
        xibei.setNumber(userInfoRepository.XiBei());
        CityData xinan = new CityData();
        xinan.setArea("西南地区");
        xinan.setNumber(userInfoRepository.XiNan());
        CityData dongbei = new CityData();
        dongbei.setArea("东北地区");
        dongbei.setNumber(userInfoRepository.DongBei());
        CityData gangaotai = new CityData();
        gangaotai.setArea("港澳台地区");
        gangaotai.setNumber(userInfoRepository.GangAoTai());
        cityDatas.add(huabei);cityDatas.add(huadong);
        cityDatas.add(huazhong);cityDatas.add(huanan);
        cityDatas.add(xibei);cityDatas.add(xinan);
        cityDatas.add(dongbei);cityDatas.add(gangaotai);
        return cityDatas;
    }

    @Override
    public Capacity cap() {
        Integer Total = 30;
        Integer vip = userInfoRepository.CountVip();
        Integer novip = userInfoRepository.CountNoVip();
        Integer Used = vip * 5 + novip * 2;
        Capacity capacity = new Capacity();
        capacity.setTotal(Total+" GB");
        capacity.setUsed(Used+" GB");
        return capacity;
    }

    @Override
    public Map<String,Object> areaData() {
        Map<String,Object> areasData = new HashMap<>();
        areasData.put("jiangsu",userInfoRepository.jiangsu());
        areasData.put("anhui",userInfoRepository.anhui());
        areasData.put("jiangxi",userInfoRepository.jiangxi());
        areasData.put("shanghai",userInfoRepository.shanghai());
        areasData.put("shandong",userInfoRepository.shandong());
        areasData.put("zhejiang",userInfoRepository.zhejiang());
        areasData.put("fujian",userInfoRepository.fujian());
        areasData.put("guangdong",userInfoRepository.guangdong());
        areasData.put("guangxi",userInfoRepository.guangxi());
        areasData.put("hainan",userInfoRepository.hainan());
        areasData.put("hubei",userInfoRepository.hubei());
        areasData.put("hunan",userInfoRepository.hunan());
        areasData.put("henan",userInfoRepository.henan());
        areasData.put("beijing",userInfoRepository.beijing());
        areasData.put("tianjin",userInfoRepository.tianjin());
        areasData.put("hebei",userInfoRepository.hebei());
        areasData.put("shanxi",userInfoRepository.shanxi());
        areasData.put("shanxi3",userInfoRepository.shanxi3());
        areasData.put("neimenggu",userInfoRepository.neimenggu());
        areasData.put("ningxia",userInfoRepository.ningxia());
        areasData.put("xinjiang",userInfoRepository.xinjiang());
        areasData.put("qinghai",userInfoRepository.qinghai());
        areasData.put("gansu",userInfoRepository.gansu());
        areasData.put("sichuan",userInfoRepository.sichuan());
        areasData.put("yunnan",userInfoRepository.yunnan());
        areasData.put("guizhou",userInfoRepository.guizhou());
        areasData.put("xizang",userInfoRepository.xizang());
        areasData.put("chongqing",userInfoRepository.chongqing());
        areasData.put("liaoning",userInfoRepository.liaoning());
        areasData.put("jilin",userInfoRepository.jilin());
        areasData.put("heilongjiang",userInfoRepository.heilongjiang());
        areasData.put("xianggang",userInfoRepository.xianggang());
        areasData.put("aomen",userInfoRepository.aomen());
        areasData.put("taiwan",userInfoRepository.taiwan());
        System.out.println(areasData.size());
        return areasData;
    }
}
