package com.example.cloudstore.domain;

import java.util.List;
import java.util.Map;

public class JsonShare {
    private String username;

    private String vip;

    private String userIntro;

    private String shareName;

    private String shareTime;

    private String type;

    private String fatherPath;

    private List<Map<String, Object>> info;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public List<Map<String, Object>> getInfo() {
        return info;
    }

    public void setInfo(List<Map<String, Object>> info) {
        this.info = info;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFatherPath() {
        return fatherPath;
    }

    public void setFatherPath(String fatherPath) {
        this.fatherPath = fatherPath;
    }
}
