package com.example.cloudstore.service;

import java.io.IOException;

public interface IconService {

    /**
     * 创建文件夹
     * 返回文件夹在文件系统的全路径
     * @param path  example:/usr/dir1; /usr
     * @throws IOException
     */
    boolean createDir(String path) throws IOException;

    /**
     * 上传头像
     * 返回头像在文件系统的全路径
     * @param src 上传的头像的源路径
     * @param dst  上传到文件系统上的目标路径
     * @throws IOException
     */
    String uploadImageFile(String src, String dst) throws IOException;


}
