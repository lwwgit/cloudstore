package com.example.cloudstore.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "orderinfo")
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String alipayName;

    private String money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlipayNo() {
        return alipayName;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayName = alipayNo;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

}
