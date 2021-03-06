package com.example.cloudstore.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FileShared {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String charId;

    /**** 分享的文件名 ***/
    private String fileName;
    /***** 文件拥有者 ***/
    private String owner;
    /***** 文件大小 ****/
    private String size;
    /****** 文件类型 ****/
    private String type;
    /**** 文件路径 ****/
    private String path;
    /**** 是否有密码 ***/
    private String ifPasswd;
    /**** 分享密码 ***/
    private String passwd;
    /**** 分享生成时间 ****/
    private String time;
    /**** 文件实际长度 ****/
    private long length;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCharId() {
        return charId;
    }

    public void setCharId(String charId) {
        this.charId = charId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIfPasswd() {
        return ifPasswd;
    }

    public void setIfPasswd(String ifPasswd) {
        this.ifPasswd = ifPasswd;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
