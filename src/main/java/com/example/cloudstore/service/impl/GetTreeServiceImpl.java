package com.example.cloudstore.service.impl;

import com.example.cloudstore.service.GetTreeService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.powermock.core.ListMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetTreeServiceImpl implements GetTreeService {

    @Value("${HDFS_PATH}")
    private String HdfsPath;
    //更改文件服务器地址


    @Override
    public List<Map<String, Object>> FindTree(String path) throws URISyntaxException, IOException {

        FileSystem hdfs = null;
        Configuration config = new Configuration();

        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);
        Path newpath = new Path(path); //确定搜索文件夹
        List<Map<String, Object>> dirMap = new ArrayList<>();

        ChildDir(hdfs, newpath, dirMap);
        for (int i = 0; i < dirMap.size(); i++) {
//            DirMap.get(i).put("Child", GrandDir(hdfs, new Path(path + "/" + DirMap.get(i).get("DirName"))));
            dirMap.get(i).put("child", GrandDir(hdfs, new Path((String) dirMap.get(i).get("Path"))));
        }

        return dirMap;
    }

    public void ChildDir(FileSystem hdfs, Path path, List<Map<String, Object>> listMap) throws IOException {

        FileStatus[] files = hdfs.listStatus(path);

        for (int j = 0; j < files.length; j++) {
            if (files[j].isDirectory()) {
                //递归调用
                Map<String, Object> list = new HashMap<>();
                list.put("dirName", files[j].getPath().getName());
                String suffix = files[j].getPath().toString().substring(files[j].getPath().toString().lastIndexOf("9000") + 4);
                list.put("path", suffix);
                listMap.add(list);
            }
        }
    }

    public String GrandDir(FileSystem hdfs, Path path) throws IOException {

        FileStatus[] files = hdfs.listStatus(path);

        for (int k = 0; k < files.length; k++) {
            if (files[k].isDirectory()) {
                return "true";
            }
        }
        return "false";
    }
}