package com.example.cloudstore.service.impl;

import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.entity.FileShared;
import com.example.cloudstore.repository.FileSharedRepository;
import com.example.cloudstore.service.FileSharedService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class FileSharedServiceImpl implements FileSharedService {


    @Value("${HDFS_PATH}")
    private String HdfsPath;

    @Autowired
    FileSharedRepository fileSharedRepository;

    @Override
    public String CreateSharedLink(String[] paths) throws URISyntaxException, IOException {

        GlobalFunction globalFunction = new GlobalFunction();

        String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < 8; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        String returnUrl = "http://localhost:8080/home/share?id=" + sb.toString();

        FileSystem hdfs = null;
        Configuration config = new Configuration();
        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);

        System.out.println("批量分享 Impl: " + paths);

        for (String path: paths) {
            System.out.println("啦啦啦啦：" + path);
            Path newpath = new Path(path);
            FileStatus file = hdfs.getFileStatus(newpath);

            String name = file.getPath().getName();
            String type = null;
            Long size = null;
            if (file.isFile()) {
                String suffix = name.substring(name.lastIndexOf(".") + 1);
                type = globalFunction.getFileType(suffix);
                size = file.getLen();
            }
            if (file.isDirectory()) {
                type = "folder";
                size = hdfs.getContentSummary(new Path(path)).getLength();
            }

            FileShared fileShared = new FileShared();
            fileShared.setCharId(sb.toString());
            fileShared.setFilename(name);
            fileShared.setOwner(file.getOwner());
            String savePath = file.getPath().toString().substring(file.getPath().toString().lastIndexOf("9000") + 4);
            fileShared.setPath(savePath);
            fileShared.setSize(size);
            fileShared.setType(type);

            fileSharedRepository.save(fileShared);
        }

        return returnUrl;
    }

    @Override
    public List<FileShared>  ToShared(String id){

        return fileSharedRepository.findAllByCharId(id);
    }
}
