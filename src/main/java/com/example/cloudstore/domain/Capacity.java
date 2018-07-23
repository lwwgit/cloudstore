package com.example.cloudstore.domain;

public class Capacity {
    private Integer total;
    private Integer used;
    private Integer notused;

    public Integer getNotused() {
        return notused;
    }

    public void setNotused(Integer notused) {
        this.notused = notused;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }
}
