package com.example.cloudstore.service;



import com.example.cloudstore.domain.AgeData;
import com.example.cloudstore.domain.Capacity;
import com.example.cloudstore.domain.CityData;
import com.example.cloudstore.domain.entity.Complaint;
import com.example.cloudstore.domain.JsonUser2Adm;
import com.example.cloudstore.domain.entity.SysUser;
import com.example.cloudstore.domain.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface AdminService {
    Page<SysUser> UserDisplay(Pageable pageable);
    UserInfo  userInfoDisplay(String username);
    List<JsonUser2Adm> vipInfoDisPlay();
    List<JsonUser2Adm> novipInfoDisPlay();
    JsonUser2Adm specificDisply(String username) throws URISyntaxException;
    int tobeVip(String username);
    int cancleVip(String username);
    int modeEnable(String username);
    int modeFreeze(String username);
    String comSub(String username, String cominfo);
    Complaint comView(String username);
    Map<String,Object> ageDistribution();
    List<CityData> cityDistribution();
    Capacity cap();
    Map<String,Object> areaData();

}
