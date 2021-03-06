package com.example.cloudstore.service.impl;

import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.JsonShare;
import com.example.cloudstore.domain.entity.FileShared;
import com.example.cloudstore.domain.entity.ShareDetails;
import com.example.cloudstore.domain.entity.UserInfo;
import com.example.cloudstore.repository.FileSharedRepository;
import com.example.cloudstore.repository.ShareDetailsRepository;
import com.example.cloudstore.repository.UserInfoRepository;
import com.example.cloudstore.service.AdminService;
import com.example.cloudstore.service.CopyFileService;
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

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    CopyFileService copyFileService;

    @Autowired
    AdminService adminService;

    @Override
    public Map<String, Object> CreateSharedLink(String[] paths, String ifPasswd) throws URISyntaxException, IOException {

        GlobalFunction globalFunction = new GlobalFunction();

        //生成id
        String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < 8; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }

        //判断是否需要分享密码并生成密码
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

        //连接文件系统
        FileSystem hdfs = null;
        Configuration config = new Configuration();
        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);


        /*********** 数据库存储总表shareDetails **********************************/
        Path path0 = new Path(paths[0]);//获得第一个分享文件的属性
        FileStatus file0 = hdfs.getFileLinkStatus(path0);
        String fp = file0.getPath().getParent().toString();//字符串截取，获得父目录
        String fatherPath = fp.substring(fp.lastIndexOf("9000") + 4);

        ShareDetails shareDetails = new ShareDetails();

        //shareDetails存入分享密码
        shareDetails.setPasswd(passwd);
        shareDetails.setIfPasswd(ifPasswd);
        shareDetails.setCharId(sb.toString());
        shareDetails.setFatherPath(fatherPath);
        /**
         * 测试的时候要更换name
         * **/
        shareDetails.setUsername(globalFunction.getUsername());
//        shareDetails.setUsername("lww");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime = formatter.format(new Date());//存入当前分享时间

        shareDetails.setTime(nowTime);
        //判断分享的文件数量，确定分享shareName和type
        int number = paths.length;
        shareDetails.setFileNum(number);
        if (number == 1) {
            shareDetails.setShareName(file0.getPath().getName());
            String fileType = file0.getPath().getName().substring(file0.getPath().getName().lastIndexOf(".") + 1);
            shareDetails.setType(globalFunction.getFileType(fileType));
            if (file0.isDirectory()){
                shareDetails.setType("folder");
            }
        }
        if (number > 1) {
            shareDetails.setShareName(file0.getPath().getName() + "等");
            shareDetails.setType("folder");
        }
        //数据库存储分享总表shareDetails
        shareDetailsRepository.save(shareDetails);


        // 数据库存储详细表 file_shared
        for (String path : paths) {
            Path newpath = new Path(path);
            FileStatus file = hdfs.getFileStatus(newpath);//获得单个文件的属性

            String name = file.getPath().getName();
            String type = null;
            String size = null;
            long length = 0;
            if (file.isFile()) {
                String suffix = name.substring(name.lastIndexOf(".") + 1);
                type = globalFunction.getFileType(suffix);
                size = globalFunction.getFileSize(file.getLen());
                length = file.getLen();
            }
            if (file.isDirectory()) {
                type = "folder";
                size = globalFunction.getFileSize(hdfs.getContentSummary(new Path(path)).getLength());
                length = hdfs.getContentSummary(new Path(path)).getLength();
            }

            FileShared fileShared = new FileShared();
            fileShared.setCharId(sb.toString());
            fileShared.setFileName(name);
            fileShared.setOwner(file.getOwner());
            String savePath = file.getPath().toString().substring(file.getPath().toString().lastIndexOf("9000") + 4);
            fileShared.setPath(savePath);
            fileShared.setSize(size);
            fileShared.setType(type);
            fileShared.setIfPasswd(ifPasswd);
            fileShared.setPasswd(passwd);
            fileShared.setTime(nowTime);
            fileShared.setLength(length);
            fileSharedRepository.save(fileShared);
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("id", sb.toString());
        returnMap.put("passwd", passwd);

        System.out.println("打印returnMap" + returnMap);
        return returnMap;
    }

    @Override
    public Map<String, Object> ShareVerify(String id, String username) {
        ShareDetails shareDetails = shareDetailsRepository.findByCharId(id);

        System.out.println("打印shareDetails: " + shareDetails);
        String ifPasswd = shareDetails.getIfPasswd();
        String shareUsername = shareDetails.getUsername();
        System.out.println("打印ifPasswd: " + ifPasswd);
        if (shareDetails.getUsername().equals(username)) {
            ifPasswd = "no";
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("ifPasswd", ifPasswd);
        returnMap.put("shareUsername", shareUsername);
        return returnMap;
    }

    @Override
    public List<ShareDetails> AllShare() {

        GlobalFunction globalFunction = new GlobalFunction();
        String username = globalFunction.getUsername();
        return shareDetailsRepository.findAllByUsername(username);
    }

    @Override
    public JsonShare ToShare(String id, String passwd) throws IOException {
        List<FileShared> list = fileSharedRepository.findAllByCharId(id);
        JsonShare jsonShare = new JsonShare();
        List<Map<String, Object>> returnList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //检查文件（夹）是否存在
            if (copyFileService.checkIsExist(list.get(i).getPath())) {

                Map<String, Object> childList = new HashMap<>();
                childList.put("fileName", list.get(i).getFileName());
                childList.put("owner", list.get(i).getOwner());
                childList.put("size", list.get(i).getSize());
                childList.put("type", list.get(i).getType());
                childList.put("path", list.get(i).getPath());
                childList.put("time", list.get(i).getTime());
                childList.put("len", list.get(i).getLength());
                returnList.add(childList);

            }
        }

        System.out.println("打印list.size: " + list.size());
        /***** 测试时修改name *****/
        ShareDetails shareDetails = shareDetailsRepository.findByCharId(id);
        String name = shareDetails.getUsername();
//            String name = "lww";

        UserInfo userInfo = userInfoRepository.findByUsername(name);

        jsonShare.setUsername(userInfo.getUsername());//分享者的名字
        jsonShare.setVip(userInfo.getVip());//分享者的用户等级
        jsonShare.setUserIntro(userInfo.getIntroduction());//分享者的个性签名
        jsonShare.setShareName(shareDetails.getShareName());//分享总名称
        jsonShare.setShareTime(shareDetails.getTime());//分享时间
        jsonShare.setType(shareDetails.getType());//分享总类型
        jsonShare.setFatherPath(shareDetails.getFatherPath());//分享列表的父目录
        jsonShare.setInfo(returnList);//分享总文件所包含的单个具体文件信息

        //检查密码是否匹配
        String verifyPasswd = shareDetailsRepository.findByCharId(id).getPasswd();
        if (passwd.equals("-1") || passwd.equals(verifyPasswd)) {
            return jsonShare;
        } else {
            return null;
        }
    }

    @Override
    public String RemoveShare(String[] strid) {
        int n = 0;
        for (String id : strid) {
            System.out.println("输入的charId" + id);
            fileSharedRepository.deleteAllByCharId(id);
            shareDetailsRepository.deleteByCharId(id);
            if (fileSharedRepository.findByCharId(id) == null && shareDetailsRepository.findByCharId(id) == null) {
                n++;
            }
        }
        if (n == strid.length) {
            return "success";
        } else {
            return "fail";
        }
    }

    @Override
    public String Report(String id) {
        ShareDetails shareDetails = shareDetailsRepository.findByCharId(id);
        int reportNum = shareDetails.getReport();
        reportNum = reportNum + 1;
        shareDetails.setReport(reportNum);
        shareDetailsRepository.save(shareDetails);

        List<ShareDetails> list = shareDetailsRepository.findAllByUsername(shareDetails.getUsername());
        int n = 0;
        for (int i = 0; i < list.size(); i ++){
            n = n + list.get(i).getReport();
        }
        if (n > 20){
            adminService.modeFreeze(shareDetails.getUsername());
        }

        ShareDetails shareDetailsResult = shareDetailsRepository.findByCharId(id);
        if (shareDetailsResult.getReport() == reportNum) {
            return "success";
        } else {
            return "fail";
        }
    }

    @Override
    public String IfSqlById(String id) {
        if (shareDetailsRepository.findByCharId(id) == null) {
            return "false";
        } else {
            return "true";
        }
    }
}
