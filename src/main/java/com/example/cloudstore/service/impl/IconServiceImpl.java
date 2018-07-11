package com.example.cloudstore.service.impl;


import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.service.IconService;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class IconServiceImpl implements IconService {

    @Autowired
    private GlobalFunction globalFunction;



    /**
     * 用户注册时在文件系统创建和用户名相同的文件夹(/user)
     * 在属于用户的文件夹下创建回收站目录（/user/tmp）
     * @param path  example:/usr/dir1; /usr
     * @throws IOException
     */
    @Override
    public boolean createDir(String path) throws IOException {
        FileSystem fs = globalFunction.getHadoopFileSystem();

        Path srcPath =  new Path(path);
        //调用mkdir（）创建目录，（可以一次性创建，以及不存在的父目录）
        boolean flag = fs.mkdirs(srcPath);
        //关闭文件系统
        fs.close();
        if(flag) {
           return true;
//            System.out.println("创建文件夹成功create dir ok!");
        }else {
            return false;
//            System.out.println("创建文件夹失败create dir failure");
        }


    }

    @Override
    public String uploadImageFile(String src, String dst) throws IOException {
        FileSystem fs = globalFunction.getHadoopFileSystem();

        String iconPath = null;
        Path srcPath = new Path(src); //原路径
        Path dstPath = new Path(dst); //目标路径
        //调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认为false
        fs.copyFromLocalFile(false,srcPath, dstPath);

        //打印文件路径
        FileStatus [] fileStatus = fs.listStatus(dstPath);
        for (FileStatus file : fileStatus) {
            System.out.println("上传的文件的路径为："+file.getPath());
            iconPath =  file.getPath().toString();
        }
        //关闭文件系统
        fs.close();
        return iconPath;
    }






}
