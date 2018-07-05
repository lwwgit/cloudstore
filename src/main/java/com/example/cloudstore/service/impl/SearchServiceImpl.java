package com.example.cloudstore.service.impl;

import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.service.SearchService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SearchServiceImpl implements SearchService {

    //更改文件服务器地址
    static String HdfsPath = "hdfs://maste:9000";

    @Override
    public List<Map<String, Object>> SearchFile(String SearchWord) throws URISyntaxException, IOException {
//        GlobalFunction globalFunction = new GlobalFunction();
//        String name = globalFunction.getUsername();
        String name = "";


        FileSystem hdfs = null;
        Configuration config = new Configuration();

        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);
        Path path = new Path("/" + name); //确定搜索文件夹  new Path("/" + dir)，dir为文件夹的绝对路径
        List<Map<String, Object>> ListMap = new ArrayList<>();

        Pattern pattern = Pattern.compile(SearchWord);//处理关键字
        ReturnSearch(hdfs, path, ListMap, pattern);//遍历目录，列出所有文件的ListMap

        return ListMap;
    }

    public void ReturnSearch(FileSystem hdfs, Path path, List<Map<String, Object>> ListMap, Pattern pattern) throws IOException {

        FileStatus[] files = hdfs.listStatus(path);//获取目录下所有文件（包括目录）的信息

        //设置时间格式
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //展示文件信息
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                //递归调用
                ReturnSearch(hdfs, files[i].getPath(), ListMap, pattern);
            }

            if (files[i].isFile()) {
                String suffix = files[i].getPath().getName().substring(files[i].getPath().getName().lastIndexOf(".") + 1);
                Matcher matcher = pattern.matcher(files[i].getPath().getName());
                while (matcher.find()) {
                    Map<String, Object> list = new HashMap<>();
                    list.put("Name", files[i].getPath().getName());
                    list.put("Path", files[i].getPath().toString());
                    list.put("ModificationTime", formatter.format(files[i].getModificationTime()));
                    list.put("length", files[i].getLen());
                    list.put("type", suffix);

                    ListMap.add(list);
                }

            }
        }
    }
}
