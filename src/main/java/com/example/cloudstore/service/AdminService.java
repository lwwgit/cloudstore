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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface AdminService {
    List<JsonUser2Adm> InfoDisplay() throws URISyntaxException;
    Page<SysUser> UserDisplay(Pageable pageable);
    UserInfo  userInfoDisplay(String username);
    List<JsonUser2Adm> vipInfoDisPlay()throws IOException,URISyntaxException;
    List<JsonUser2Adm> novipInfoDisPlay() throws IOException,URISyntaxException;
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
    List<Map<String, Object>>  userReport();
    Map<String,Object> CountData();
    Boolean subFailure(String username);
}
