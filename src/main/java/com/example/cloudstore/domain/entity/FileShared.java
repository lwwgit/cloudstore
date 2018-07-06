package com.example.cloudstore.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FileShared {
    @Id
    private String id;

    /**** 分享的文件名 ***/
    private String filename;
    /***** 文件拥有者 ***/
    private String owner;
    /***** 文件大小 ****/
    private double size;
    /****** 文件类型 ****/
    private String type;
    /**** 文件路径 ****/
    private String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
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
}
