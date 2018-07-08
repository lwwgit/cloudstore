package com.example.cloudstore.service.impl;

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
import java.util.Optional;

@Service
public class FileSharedServiceImpl implements FileSharedService {


    @Value("${HDFS_PATH}")
    private String HdfsPath;

    @Autowired
    FileSharedRepository fileSharedRepository;

    @Override
    public String CreateSharedLink(String path) throws URISyntaxException, IOException {

        String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < 8; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        String returnUrl = "http://localhost:8080/share.html?id=" + sb.toString();


        FileSystem hdfs = null;
        Configuration config = new Configuration();
        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);
        Path newpath = new Path(path);
        FileStatus file = hdfs.getFileStatus(newpath);


        String name = file.getPath().getName();
        String type = null;
        Long size = null;
        if (file.isFile()){
            type = name.substring(name.lastIndexOf(".") + 1);
            size = file.getLen();
        }
        if (file.isDirectory()){
            type = "dir";
            size = hdfs.getContentSummary(new Path(path)).getLength();
        }

        FileShared fileShared = new FileShared();
        fileShared.setId(sb.toString());
        fileShared.setFilename(name);
        fileShared.setOwner(file.getOwner());
        fileShared.setPath(file.getPath().toString());
        fileShared.setSize(size);
        fileShared.setType(type);

        fileSharedRepository.save(fileShared);

        return returnUrl;
    }

    @Override
    public Optional<FileShared> ToShared(String id){

        return fileSharedRepository.findById(id);
    }
}
