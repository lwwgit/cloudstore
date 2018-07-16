package com.example.cloudstore.service.impl;

import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.entity.FileShared;
import com.example.cloudstore.domain.entity.ShareDetails;
import com.example.cloudstore.repository.FileSharedRepository;
import com.example.cloudstore.repository.ShareDetailsRepository;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileSharedServiceImpl implements FileSharedService {


    @Value("${HDFS_PATH}")
    private String HdfsPath;

    @Autowired
    FileSharedRepository fileSharedRepository;

    @Autowired
    ShareDetailsRepository shareDetailsRepository;

    @Override
    public Map<String, Object> CreateSharedLink(String[] paths, String ifPasswd) throws URISyntaxException, IOException {

        GlobalFunction globalFunction = new GlobalFunction();

        /**** 连接文件系统 *****/
        FileSystem hdfs = null;
        Configuration config = new Configuration();
        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);

        /*** 生成id ***/
        String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < 8; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        String returnUrl = "http://localhost:8080/home/share?id=" + sb.toString();

        //获得第一个分享文件的属性
        Path path0 = new Path(paths[0]);
        FileStatus file0 = hdfs.getFileStatus(path0);
        ShareDetails shareDetails = new ShareDetails();
        shareDetails.setCharId(sb.toString());
        /*** 测试的时候要更换name ****/
        shareDetails.setUsername(globalFunction.getUsername());
//        shareDetails.setUsername("lww");

        //判断分享的文件数量，确定分享文件名和类型
        int number = paths.length;
        shareDetails.setFileNum(number);
        if (number == 1) {
            shareDetails.setShareName(file0.getPath().getName());
            shareDetails.setType(file0.getPath().getName().substring(file0.getPath().getName().lastIndexOf(".") + 1));
        }
        if (number > 1) {
            shareDetails.setShareName(file0.getPath().getName() + "等");
            shareDetails.setType("folder");
        }

        /**** 判断是否需要分享密码并生成密码 ***/
        String passwd = "-1";
        if (ifPasswd.equals("yes")) {
            String YesString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuffer str = new StringBuffer();
            int strLen = YesString.length();
            for (int i = 0; i < 4; i++) {
                str.append(YesString.charAt((int) Math.round(Math.random() * (strLen - 1))));
            }
            passwd = str.toString();
        }
        //shareDetails存入分享密码
        shareDetails.setPasswd(passwd);
        shareDetails.setIfPasswd(ifPasswd);
        shareDetailsRepository.save(shareDetails);

        for (String path : paths) {
            Path newpath = new Path(path);
            FileStatus file = hdfs.getFileStatus(newpath);//获得单个文件的属性

            String name = file.getPath().getName();
            String type = null;
            String size = null;
            if (file.isFile()) {
                String suffix = name.substring(name.lastIndexOf(".") + 1);
                type = globalFunction.getFileType(suffix);
                size = globalFunction.getFileSize(file.getLen());
            }
            if (file.isDirectory()) {
                type = "folder";
                size = globalFunction.getFileSize(hdfs.getContentSummary(new Path(path)).getLength());
            }

            FileShared fileShared = new FileShared();
            fileShared.setCharId(sb.toString());
            fileShared.setFilename(name);
            fileShared.setOwner(file.getOwner());
            String savePath = file.getPath().toString().substring(file.getPath().toString().lastIndexOf("9000") + 4);
            fileShared.setPath(savePath);
            fileShared.setSize(size);
            fileShared.setType(type);
            fileShared.setIfPasswd(ifPasswd);
            fileShared.setPasswd(passwd);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = formatter.format(new Date());

            fileShared.setTime(nowTime);

            fileSharedRepository.save(fileShared);
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("openUrl", returnUrl);
        returnMap.put("passwd", passwd);

        return returnMap;
    }

    @Override
    public String ShareVerify(String id) {
        ShareDetails shareDetails = shareDetailsRepository.findByCharId(id);
        return shareDetails.getIfPasswd();
    }

    @Override
    public List<ShareDetails> AllShare() {
        return shareDetailsRepository.findAll();
    }

    @Override
    public List<Map<String, Object>> ToShare(String id, String passwd) {
        List<FileShared> list = fileSharedRepository.findAllByCharId(id);

        List<Map<String, Object>> returnList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> childList = new HashMap<>();
            childList.put("filename", list.get(i).getFilename());
            childList.put("owner", list.get(i).getOwner());
            childList.put("size", list.get(i).getSize());
            childList.put("type", list.get(i).getType());
            childList.put("path", list.get(i).getPath());
            returnList.add(childList);
        }
        if (passwd.equals("-1") || passwd.equals(list.get(0).getPasswd())) {
            return returnList;
        } else {
            return null;
        }
    }

    @Override
    public String RemoveShare(String id) {

        fileSharedRepository.deleteAllByCharId(id);
        shareDetailsRepository.deleteByCharId(id);

        if (fileSharedRepository.findByCharId(id) == null && shareDetailsRepository.findByCharId(id) == null) {
            return "success";
        } else {
            return "fail";
        }
    }
}
