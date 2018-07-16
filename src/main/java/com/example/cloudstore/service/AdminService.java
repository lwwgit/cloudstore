package com.example.cloudstore.service;



import com.example.cloudstore.domain.AgeData;
import com.example.cloudstore.domain.CityData;
import com.example.cloudstore.domain.entity.Complaint;
import com.example.cloudstore.domain.JsonUser2Adm;

import java.util.List;

public interface AdminService {
    List<JsonUser2Adm> InfoDisplay();
    List<JsonUser2Adm> vipInfoDisPlay();
    List<JsonUser2Adm> novipInfoDisPlay();
    JsonUser2Adm specificDisply(String username);
    int tobeVip(String username);
    int cancleVip(String username);
    int modeEnable(String username);
    int modeFreeze(String username);
    String comSub(String username, String cominfo);
    Complaint comView(String username);
    List<AgeData> ageDistribution();
    List<CityData> cityDistribution();

}
