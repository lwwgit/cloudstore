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

        if (fileStatus.isDirectory()){
            return String.valueOf(hdfs.getContentSummary(new Path(path)).getLength());
        }
        if (fileStatus.isFile()){
            return "Error. It's not a directory";
        }
        return "Error.Input is error";
    }


    /**
     * 根据配置文件获取HDFS操作对象
     * @return
     * @throws IOException
     */
    public FileSystem getHadoopFileSystem() throws IOException {
        //读取配置文件
        Configuration conf = new Configuration();
        FileSystem fs = null;
        try {
            // 根据配置文件创建HDFS对象
            fs = FileSystem.get(URI.create(HADOOP_URL),conf);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("",e);
        }
        return fs;
    }

}
