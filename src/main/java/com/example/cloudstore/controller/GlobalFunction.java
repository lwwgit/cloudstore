package com.example.cloudstore.controller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class GlobalFunction {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${HDFS_PATH}")
    private String HADOOP_URL;


    /**
     * 获取当前登录用户的用户名
     *
     * @return
     */
    public String getUsername() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
//        String username = userDetails.getUsername();
//        return username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }

    //获取文件类型type
    public String getFileType(String suffix) {
        String[] docType = {"doc", "docx"};
        String[] pdfType = {"pdf"};
        String[] pptType = {"ppt", "pptx"};
        String[] txtType = {"txt"};
        String[] xlsType = {"xls", "xlsx"};
        String[] codeType = {"c", "java", "h", "html", "css", "php", "jsp", "cpp", "md"};
        String[] imgType = {"jpg", "png", "gif", "jpeg", "bmp"};
        String[] musicType = {"wav", "mp3", "wma", "aac", "flac", "ram", "m4a"};
        String[] videoType = {"avi", "mov", "mp4", "wmv", "mkv", "flv"};
        String[] zipType = {"rar", "zip", "gz", "arj", "z"};
        String[] exeType = {"exe"};
        String[] apkType = {"apk"};

        for (String str : docType) {
            if (suffix.equals(str)) {
                return ("doc");
            }
        }
        for (String str : pdfType) {
            if (suffix.equals(str)) {
                return ("pdf");
            }
        }
        for (String str : pptType) {
            if (suffix.equals(str)) {
                return ("ppt");
            }
        }
        for (String str : txtType) {
            if (suffix.equals(str)) {
                return ("txt");
            }
        }
        for (String str : xlsType) {
            if (suffix.equals(str)) {
                return ("xls");
            }
        }
        for (String str : codeType) {
            if (suffix.equals(str)) {
                return ("code");
            }
        }
        for (String str : imgType) {
            if (suffix.equals(str)) {
                return ("img");
            }
        }
        for (String str : musicType) {
            if (suffix.equals(str)) {
                return ("music");
            }
        }
        for (String str : videoType) {
            if (suffix.equals(str)) {
                return ("video");
            }
        }
        for (String str : zipType) {
            if (suffix.equals(str)) {
                return ("zip");
            }
        }
        for (String str : exeType) {
            if (suffix.equals(str)) {
                return ("exe");
            }
        }
        for (String str : apkType) {
            if (suffix.equals(str)) {
                return ("apk");
            }
        }
        return "others";
    }


    //获取文件夹大小
    public String getDirectorySize(String path) throws URISyntaxException, IOException {
        FileSystem hdfs = null;
        Configuration config = new Configuration();
        // 程序配置
        config.set("fs.default.name", HADOOP_URL);
        //config.set("hadoop.job.ugi", "feng,111111");
        //config.set("hadoop.tmp.dir", "/tmp/hadoop-fengClient");
        //config.set("dfs.replication", "1");
        //config.set("mapred.job.tracker", "master:9001");
        hdfs = FileSystem.get(new URI(HADOOP_URL), config);
//        Path path = new Path("/");
        Path newPath = new Path(path);
        FileStatus fileStatus = hdfs.getFileStatus(newPath);
//        System.out.println("dir3的文件夹大小是：" + hdfs.getContentSummary(new Path(path)).getLength());

        if (fileStatus.isDirectory()) {
            return String.valueOf(hdfs.getContentSummary(new Path(path)).getLength());
        }
        if (fileStatus.isFile()) {
            return "Error. It's not a directory";
        }
        return "Error.Input is error";
    }


    /**
     * 根据配置文件获取HDFS操作对象
     *
     * @return
     * @throws IOException
     */
    public FileSystem getHadoopFileSystem() throws IOException {
        //读取配置文件
        Configuration conf = new Configuration();
        FileSystem fs = null;
        try {
            // 根据配置文件创建HDFS对象
            fs = FileSystem.get(URI.create(HADOOP_URL), conf);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("", e);
        }
        return fs;
    }

}
