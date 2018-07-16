package com.example.cloudstore.service.impl;


import com.example.cloudstore.domain.AgeData;
import com.example.cloudstore.domain.CityData;
import com.example.cloudstore.domain.JsonUser2Adm;
import com.example.cloudstore.domain.entity.Complaint;
import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.domain.entity.UserInfo;
import com.example.cloudstore.repository.ComplaintRepository;
import com.example.cloudstore.repository.SysUserRepository;
import com.example.cloudstore.repository.UserInfoRepository;
import com.example.cloudstore.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Override
    public List<JsonUser2Adm> InfoDisplay() {
        List<JsonUser2Adm> jsonUser2Adms = new ArrayList<>();
        List<SysUser> sysUsers = sysUserRepository.findAll();

        for (int i=0;i<sysUsers.size();i++){
            JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
            String username = sysUsers.get(i).getUsername();
            UserInfo userInfo = userInfoRepository.findByUsername(username);
            jsonUser2Adm.setCity(userInfo.getCity());
            Date date = sysUsers.get(i).getCreateDate();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
            jsonUser2Adm.setCreate_date(dateStr);
            jsonUser2Adm.setId(userInfo.getId());
            jsonUser2Adm.setSex(userInfo.getSex());
            jsonUser2Adm.setUsername(userInfo.getUsername());
            String vip = userInfo.getVip();
            if (vip.equals("1")){
                jsonUser2Adm.setVip("vip用户");
            }else {
                jsonUser2Adm.setVip("普通用户");
            }
            Integer com = sysUsers.get(i).getCom();
            if (com.equals(1)){
                jsonUser2Adm.setCom("已申诉");
            }else {
                jsonUser2Adm.setCom("未申诉");
            }
            Integer state = sysUsers.get(i).getState();
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
    public JsonUser2Adm specificDisply(String username) {
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
        String vip = userInfo.getVip();
        if (vip.equals("1")){
            jsonUser2Adm.setVip("vip用户");
        }else {
            jsonUser2Adm.setVip("普通用户");
        }
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
    public List<AgeData> ageDistribution() {
        List<AgeData> ageDatas = new ArrayList<>();
        AgeData zero2ten = new AgeData();
        zero2ten.setAgeGroup("0-10");
        zero2ten.setNumber(userInfoRepository.Zero2Ten());
        AgeData ten2twenty = new AgeData();
        ten2twenty.setAgeGroup("10-20");
        ten2twenty.setNumber(userInfoRepository.Ten2Twenty());
        AgeData twenty2thirty = new AgeData();
        twenty2thirty.setAgeGroup("20-30");
        twenty2thirty.setNumber(userInfoRepository.Twenty2Thirty());
        AgeData thirty2forty = new AgeData();
        thirty2forty.setAgeGroup("30-40");
        thirty2forty.setNumber(userInfoRepository.Thirty2Forty());
        AgeData forty2fifty = new AgeData();
        forty2fifty.setAgeGroup("40-50");
        forty2fifty.setNumber(userInfoRepository.Forty2Fifty());
        AgeData fifty2sixty = new AgeData();
        fifty2sixty.setAgeGroup("50-60");
        fifty2sixty.setNumber(userInfoRepository.Fifty2Sixty());
        AgeData moreThanSixty = new AgeData();
        moreThanSixty.setAgeGroup("60以上");
        moreThanSixty.setNumber(userInfoRepository.MoerThanSixty());
        ageDatas.add(zero2ten);
        ageDatas.add(ten2twenty);
        ageDatas.add(twenty2thirty);
        ageDatas.add(thirty2forty);
        ageDatas.add(forty2fifty);
        ageDatas.add(fifty2sixty);
        ageDatas.add(moreThanSixty);
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
}
