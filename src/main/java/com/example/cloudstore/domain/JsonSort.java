package com.example.cloudstore.domain;

import java.util.List;
import java.util.Map;

public class JsonSort {
    private String isVip;
    private List<Map<String, Object>> dataList;

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }
}
