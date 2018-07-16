package com.example.cloudstore.domain;

import javax.persistence.*;

/**
 * @Author jitdc
 * @Date Create in 14:42 2018/7/13
 * @Description:
 */
@Entity
@Table(name = "privatespace")
public class PrivateSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String privatePassword;

    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivatePassword() {
        return privatePassword;
    }

    public void setPrivatePassword(String privatePassword) {
        this.privatePassword = privatePassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
