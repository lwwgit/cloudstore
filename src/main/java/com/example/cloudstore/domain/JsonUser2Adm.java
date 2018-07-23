package com.example.cloudstore.domain;

public class JsonUser2Adm {
    private Integer id;
    private String username;
    private String sex;
    private Integer vip;
    private Integer age;
    private String city;
    private Integer state;
    private String stateStr;
    private String create_date;
    private String totalsize;
    private String usedsize;
    private Float usedInfo;
    private String introduction;
    private String mobile;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public Float getUsedInfo() {
        return usedInfo;
    }

    public void setUsedInfo(Float usedInfo) {
        this.usedInfo = usedInfo;
    }

    public String getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(String totalsize) {
        this.totalsize = totalsize;
    }

    public String getUsedsize() {
        return usedsize;
    }

    public void setUsedsize(String usedsize) {
        this.usedsize = usedsize;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
