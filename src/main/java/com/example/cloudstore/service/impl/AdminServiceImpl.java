package com.example.cloudstore.service.impl;


import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.*;
import com.example.cloudstore.domain.entity.*;
import com.example.cloudstore.repository.*;
import com.example.cloudstore.service.AdminService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private ShareDetailsRepository shareDetailsRepository;


    public List<Integer> User_id(){
        List<SysUserRole> user = sysUserRoleRepository.findByRoleId(2);//普通管理员
        List<Integer> user_id = new ArrayList<>();
        for(int i = 0;i<user.size();i++){
            Integer userid = user.get(i).getUserId();
            user_id.add(userid);
        }
        return user_id;
    }
    public List<SysUser> users(){
        List<Integer> user_id = User_id();
        List<SysUser> users = new ArrayList<>();
        for (int i=0;i<user_id.size();i++){
            SysUser user = sysUserRepository.findSysUserById(user_id.get(i));
            users.add(user);
        }
        return users;
    }

    @Override
    public List<JsonUser2Adm> InfoDisplay() throws URISyntaxException {
        List<JsonUser2Adm> jsonUser2Adms = new ArrayList<>();
        List<SysUser> sysUsers = users();

        for (int i = 0; i < sysUsers.size(); i++) {
            JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
            String username = sysUsers.get(i).getUsername();
            UserInfo userInfo = userInfoRepository.findByUsername(username);
            jsonUser2Adm.setCity(userInfo.getCity());
            Date date = sysUsers.get(i).getCreateDate();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
            jsonUser2Adm.setCreate_date(dateStr);
            jsonUser2Adm.setId(userInfo.getId());
            jsonUser2Adm.setSex(userInfo.getSex());
            jsonUser2Adm.setUsername(userInfo.getUsername());
            Integer state = sysUsers.get(i).getState();
            Integer com = sysUsers.get(i).getCom();
            jsonUser2Adm.setIntroduction(userInfo.getIntroduction());
            jsonUser2Adm.setMobile(sysUsers.get(i).getTel());
            jsonUser2Adm.setAge(userInfo.getAge());
            if (com.equals(1)&&state.equals(0)) {
                jsonUser2Adm.setStateStr("已申诉");
                jsonUser2Adm.setState(1);
            } else if (com.equals(0)&&state.equals(0)){
                jsonUser2Adm.setStateStr("未申诉");
                jsonUser2Adm.setState(2);
            } else if (com.equals(0)&&state.equals(1)){
                jsonUser2Adm.setStateStr("账号正常");
                jsonUser2Adm.setState(0);
            }else {
                jsonUser2Adm.setState(3);
                jsonUser2Adm.setStateStr("已驳回");
            }
            try {
                GlobalFunction globalFunction = new GlobalFunction();
                String size = globalFunction.getDirectorySize("/" + username);
                long Lsize = Long.parseLong(size);
                String Ssize = globalFunction.getFileSize(Lsize);
                jsonUser2Adm.setUsedsize(Ssize);
                String vip = userInfo.getVip();
                if (vip.equals("1")) {
                    jsonUser2Adm.setTotalsize("5 GB");
                    jsonUser2Adm.setVip(1);
                    Float usedsize = (float) Lsize / (5 * 1000 * 1000 * 1000);
                    BigDecimal bb = new BigDecimal(usedsize*100);
                    float f1 = bb.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
                    jsonUser2Adm.setUsedInfo(f1);
                } else {
                    jsonUser2Adm.setTotalsize("2 GB");
                    jsonUser2Adm.setVip(0);
                    Float usedsize = (float) Lsize / (2 * 1000 * 1000 * 1000);
                    BigDecimal b = new BigDecimal(usedsize*100);
                    float f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
                    jsonUser2Adm.setUsedInfo(f1);
                }
            } catch (IOException e) {
                e.getMessage();
            }
            jsonUser2Adms.add(jsonUser2Adm);
        }
        return jsonUser2Adms;
    }

    @Override
    public Page<SysUser> UserDisplay(Pageable pageable) {
        return sysUserRepository.findAll(pageable);
    }

    @Override
    public UserInfo userInfoDisplay(String username) {
        return userInfoRepository.findByUsername(username);
    }

    @Override
    public List<JsonUser2Adm> vipInfoDisPlay() throws IOException, URISyntaxException {
        List<UserInfo> userInfos = userInfoRepository.userInfoSelectVip();
        List<JsonUser2Adm> jsonUser2Adms = new ArrayList<>();
        for (int i = 0; i < userInfos.size(); i++) {
            JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
            String username = userInfos.get(i).getUsername();
            SysUser user = sysUserRepository.findByUsername(username);
            Date date = user.getCreateDate();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
            jsonUser2Adm.setCreate_date(dateStr);
            jsonUser2Adm.setId(userInfos.get(i).getId());
            jsonUser2Adm.setVip(1);
            jsonUser2Adm.setMobile(user.getTel());
            jsonUser2Adm.setAge(userInfos.get(i).getAge());
            GlobalFunction globalFunction = new GlobalFunction();
            String size = globalFunction.getDirectorySize("/" + username);
            long Lsize = Long.parseLong(size);
            String Ssize = globalFunction.getFileSize(Lsize);
            jsonUser2Adm.setUsedsize(Ssize);
            jsonUser2Adm.setTotalsize("5 GB");
            Float usedsize = (float) Lsize / (5 * 1000 * 1000 * 1000);
            BigDecimal bb = new BigDecimal(usedsize*100);
            float f1 = bb.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
            jsonUser2Adm.setUsedInfo(f1);
            jsonUser2Adm.setSex(userInfos.get(i).getSex());
            jsonUser2Adm.setCity(userInfos.get(i).getCity());
            jsonUser2Adm.setUsername(username);
            Integer com = user.getCom();
            Integer state = user.getState();
            if (com.equals(1)&&state.equals(0)) {
                jsonUser2Adm.setStateStr("已申诉");
                jsonUser2Adm.setState(1);
            } else if (com.equals(0)&&state.equals(0)){
                jsonUser2Adm.setStateStr("未申诉");
                jsonUser2Adm.setState(2);
            } else if (com.equals(0)&&state.equals(1)){
                jsonUser2Adm.setState(0);
                jsonUser2Adm.setStateStr("账号正常");
            }else {
                jsonUser2Adm.setStateStr("已驳回");
                jsonUser2Adm.setState(3);
            }
            jsonUser2Adm.setIntroduction(userInfos.get(i).getIntroduction());
            jsonUser2Adms.add(jsonUser2Adm);
        }
        return jsonUser2Adms;
    }

    @Override
    public List<JsonUser2Adm> novipInfoDisPlay() throws IOException, URISyntaxException {
        List<UserInfo> userInfos = userInfoRepository.userInfoSelectNotVip();
        List<JsonUser2Adm> jsonUser2Adms = new ArrayList<>();
        for (int i = 0; i < userInfos.size(); i++) {
            JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
            String username = userInfos.get(i).getUsername();
            SysUser user = sysUserRepository.findByUsername(username);
            Date date = user.getCreateDate();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
            jsonUser2Adm.setCreate_date(dateStr);
            jsonUser2Adm.setAge(userInfos.get(i).getAge());
            Integer com = user.getCom();
            Integer state = user.getState();
            if (com.equals(1)&&state.equals(0)) {
                jsonUser2Adm.setStateStr("已申诉");
                jsonUser2Adm.setState(1);
            } else if (com.equals(0)&&state.equals(0)){
                jsonUser2Adm.setState(2);
                jsonUser2Adm.setStateStr("未申诉");
            } else if (com.equals(0)&&state.equals(1)){
                jsonUser2Adm.setState(0);
                jsonUser2Adm.setStateStr("账号正常");
            }else {
                jsonUser2Adm.setState(3);
                jsonUser2Adm.setStateStr("已驳回");
            }
            jsonUser2Adm.setId(userInfos.get(i).getId());
            jsonUser2Adm.setVip(0);
            jsonUser2Adm.setMobile(user.getTel());
            GlobalFunction globalFunction = new GlobalFunction();
            String size = globalFunction.getDirectorySize("/" + username);
            long Lsize = Long.parseLong(size);
            String Ssize = globalFunction.getFileSize(Lsize);
            jsonUser2Adm.setUsedsize(Ssize);
            jsonUser2Adm.setTotalsize("2 GB");
            Float usedsize = (float) Lsize / (2 * 1000 * 1000 * 1000);
            BigDecimal bb = new BigDecimal(usedsize*100);
            float f1 = bb.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
            jsonUser2Adm.setUsedInfo(f1);
            jsonUser2Adm.setSex(userInfos.get(i).getSex());
            jsonUser2Adm.setCity(userInfos.get(i).getCity());
            jsonUser2Adm.setUsername(username);
            jsonUser2Adm.setIntroduction(userInfos.get(i).getIntroduction());
            jsonUser2Adms.add(jsonUser2Adm);
        }
        return jsonUser2Adms;
    }

    @Override
    public JsonUser2Adm specificDisply(String username) throws URISyntaxException {
        JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
        SysUser sysUser = sysUserRepository.findByUsername(username);
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        jsonUser2Adm.setCity(userInfo.getCity());
        Date date = sysUser.getCreateDate();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
        jsonUser2Adm.setCreate_date(dateStr);
        jsonUser2Adm.setId(userInfo.getId());
        jsonUser2Adm.setSex(userInfo.getSex());
        jsonUser2Adm.setUsername(userInfo.getUsername());
        Integer com = sysUser.getCom();
        Integer state = sysUser.getState();
        jsonUser2Adm.setIntroduction(userInfo.getIntroduction());
        jsonUser2Adm.setMobile(sysUser.getTel());
        jsonUser2Adm.setAge(userInfo.getAge());
        if (com.equals(1)&&state.equals(0)) {
            jsonUser2Adm.setState(1);
            jsonUser2Adm.setStateStr("已申诉");
        } else if (com.equals(0)&&state.equals(0)){
            jsonUser2Adm.setStateStr("未申诉");
            jsonUser2Adm.setState(2);
        } else if(com.equals(0)&&state.equals(1)) {
            jsonUser2Adm.setStateStr("账号正常");
            jsonUser2Adm.setState(0);
        } else {
            jsonUser2Adm.setStateStr("已驳回");
            jsonUser2Adm.setState(3);
        }
        GlobalFunction globalFunction = new GlobalFunction();
        try {
            String size = globalFunction.getDirectorySize("/" + username);
            long Lsize = Long.parseLong(size);
            String Ssize = globalFunction.getFileSize(Lsize);
            jsonUser2Adm.setUsedsize(Ssize);
            String vip = userInfo.getVip();
            if (vip.equals("1")) {
                jsonUser2Adm.setVip(1);
                jsonUser2Adm.setTotalsize("5 GB");
                Float usedsize = (float) Lsize / (5 * 1000 * 1000 * 1000);
                BigDecimal b = new BigDecimal(usedsize*100);
                float f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
                jsonUser2Adm.setUsedInfo(f1);
            } else {
                jsonUser2Adm.setVip(0);
                jsonUser2Adm.setTotalsize("2 GB");
                Float usedsize = (float) Lsize / (2 * 1000 * 1000 * 1000);
                BigDecimal b = new BigDecimal(usedsize);
                float f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
                jsonUser2Adm.setUsedInfo(f1);
            }
        } catch (IOException e) {
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
    @Transactional
    public String comSub(String username, String cominfo) {
        SysUser sysUser = sysUserRepository.findByUsername(username);
        if (sysUser.getState().equals(1)) {
            return "账号正常使用，不用申诉";
        }
        if (sysUser.getCom().equals(1)) {
            return "已经申诉过了，请稍后再试";
        }
        if (sysUser.getCom().equals(2)){
            complaintRepository.deleteComplaintByUsername(username);
            sysUserRepository.comCan(username);
            return "您之前提交的申诉审核失败,请重新申诉";
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
    public Map<String, Object> ageDistribution() {
        Map<String, Object> ageDatas = new HashMap<>();
        ageDatas.put("shisuiyixia", userInfoRepository.shisuiyixia());
        ageDatas.put("shi2shijiu", userInfoRepository.shi2shijiu());
        ageDatas.put("ershi2erjiu", userInfoRepository.ershi2erjiu());
        ageDatas.put("sanshi2sanjiu", userInfoRepository.sanshi2sanjiu());
        ageDatas.put("sishi2sijiu", userInfoRepository.sishi2sijiu());
        ageDatas.put("wushi2wujiu", userInfoRepository.wushi2wujiu());
        ageDatas.put("liushiyishang", userInfoRepository.liushiyishang());

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
        cityDatas.add(huabei);
        cityDatas.add(huadong);
        cityDatas.add(huazhong);
        cityDatas.add(huanan);
        cityDatas.add(xibei);
        cityDatas.add(xinan);
        cityDatas.add(dongbei);
        cityDatas.add(gangaotai);
        return cityDatas;
    }

    @Override
    public Capacity cap() {
        Integer Total = 30;
        Integer vip = userInfoRepository.CountVip();
        Integer novip = userInfoRepository.CountNoVip()-sysUserRoleRepository.countAdmin();
        Integer Used = vip * 5 + novip * 2;
        Capacity capacity = new Capacity();
        capacity.setTotal(Total);
        capacity.setUsed(Used);
        capacity.setNotused(Total-Used);
        return capacity;
    }

    @Override
    public Map<String, Object> areaData() {
        Map<String, Object> areasData = new HashMap<>();
        areasData.put("jiangsu", userInfoRepository.jiangsu());
        areasData.put("anhui", userInfoRepository.anhui());
        areasData.put("jiangxi", userInfoRepository.jiangxi());
        areasData.put("shanghai", userInfoRepository.shanghai());
        areasData.put("shandong", userInfoRepository.shandong());
        areasData.put("zhejiang", userInfoRepository.zhejiang());
        areasData.put("fujian", userInfoRepository.fujian());
        areasData.put("guangdong", userInfoRepository.guangdong());
        areasData.put("guangxi", userInfoRepository.guangxi());
        areasData.put("hainan", userInfoRepository.hainan());
        areasData.put("hubei", userInfoRepository.hubei());
        areasData.put("hunan", userInfoRepository.hunan());
        areasData.put("henan", userInfoRepository.henan());
        areasData.put("beijing", userInfoRepository.beijing());
        areasData.put("tianjin", userInfoRepository.tianjin());
        areasData.put("hebei", userInfoRepository.hebei());
        areasData.put("shanxi", userInfoRepository.shanxi());
        areasData.put("shanxi3", userInfoRepository.shanxi3());
        areasData.put("neimenggu", userInfoRepository.neimenggu());
        areasData.put("ningxia", userInfoRepository.ningxia());
        areasData.put("xinjiang", userInfoRepository.xinjiang());
        areasData.put("qinghai", userInfoRepository.qinghai());
        areasData.put("gansu", userInfoRepository.gansu());
        areasData.put("sichuan", userInfoRepository.sichuan());
        areasData.put("yunnan", userInfoRepository.yunnan());
        areasData.put("guizhou", userInfoRepository.guizhou());
        areasData.put("xizang", userInfoRepository.xizang());
        areasData.put("chongqing", userInfoRepository.chongqing());
        areasData.put("liaoning", userInfoRepository.liaoning());
        areasData.put("jilin", userInfoRepository.jilin());
        areasData.put("heilongjiang", userInfoRepository.heilongjiang());
        areasData.put("xianggang", userInfoRepository.xianggang());
        areasData.put("aomen", userInfoRepository.aomen());
        areasData.put("taiwan", userInfoRepository.taiwan());
        System.out.println(areasData.size());
        return areasData;
    }

    @Override
    public List<Map<String, Object>> userReport() {
        List<Map<String, Object>> data = new ArrayList<>();
        List<SysUserRole> sysUserRoles = sysUserRoleRepository.findByRoleId(2);
        List<String> usernames = new ArrayList<>();
        for (int i = 0; i < sysUserRoles.size(); i++) {
            String username = sysUserRepository.findSysUserById(sysUserRoles.get(i).getUserId()).getUsername();
            usernames.add(username);
        }
        System.out.println("=======" + usernames.toString());
        for (int i = 0; i < usernames.size(); i++) {
            Map<String, Object> userReport = new HashMap<>();
            List<ShareDetails> shareDetails = shareDetailsRepository.findByUsername(usernames.get(i));
            Integer count = 0;
            for (int j = 0; j < shareDetails.size(); j++) {
                count += shareDetails.get(i).getReport();
            }
            userReport.put("username", usernames.get(i));
            userReport.put("count", count);
            data.add(userReport);
            if (count.equals(5)) {
                modeFreeze(usernames.get(i));
                System.out.println(usernames.get(i) + ":该账号已被冻结");
            }
        }
        return data;
    }

    @Override
    public Map<String, Object> CountData() {
        Map<String, Object> map = new HashMap<>();
        map.put("vip",userInfoRepository.CountVip());
        map.put("users",sysUserRepository.countUser());
        map.put("check",complaintRepository.countComplaint());
        map.put("admin",sysUserRoleRepository.countAdmin());
        return map;
    }

    @Override
    public Boolean subFailure(String username) {
        int i = sysUserRepository.subFailure(username);
        if (i==1){
            return true;
        }
        return false;
    }
}
