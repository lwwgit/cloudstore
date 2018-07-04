package com.example.cloudstore.service.impl;

import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.service.SortService;
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

@Service
public class SortServiceImpl implements SortService {

    //更改文件服务器地址
    static String HdfsPath = "hdfs://maste:9000";

    @Override
    public List<Map<String, Object>> SortFile(int flag) throws URISyntaxException, IOException {
        GlobalFunction globalFunction = new GlobalFunction();

        /**************  确定用户文件夹  *******************/
//        String name = globalFunction.getUsername();
//        String name = null;
        /*************************************************/
        FileSystem hdfs = null;
        Configuration config = new Configuration();

        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);
        Path path = new Path("/");/**********确定用户文件夹  new Path("/" + name)***********/
        List<Map<String, Object>> ListMap = new ArrayList<>();

        ShowFile(hdfs, path, ListMap, flag);
        System.out.println("############" + ListMap);
        return ListMap;
    }

    public void ShowFile(FileSystem hdfs, Path path,
                         List<Map<String, Object>> ListMap, int flag) throws IOException {

        FileStatus[] files = hdfs.listStatus(path);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //展示文件信息
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory())
                //递归调用
                ShowFile(hdfs, files[i].getPath(), ListMap, flag);

            if (files[i].isFile()) {

                String name = files[i].getPath().getName();
                String suffix = name.substring(name.lastIndexOf(".") + 1);

                Map<String, Object> list = new HashMap<>();
                list.put("Name", files[i].getPath().getName());
                list.put("Path", files[i].getPath().toString());
                list.put("LastEditTime", formatter.format(files[i].getAccessTime()));
                list.put("length", files[i].getLen());

                String[] doc = new String[]{"docx", "doc", "xlsx", "xls", "pptx", "ppt", "txt", "pdf"};
                String[] pict = new String[]{"jpg", "png", "gif", "jpeg"};
                String[] video = new String[]{"avi", "mov", "mp4", "wmv", "mkv", "flv"};
                String[] music = new String[]{"wav", "mp3", "wma", "aac", "flac"};
                String[] other = new String[]{
                        "docx", "doc", "xlsx", "xls", "pptx", "ppt", "txt", "pdf",
                        "jpg", "png", "gif", "jpeg",
                        "avi", "mov", "mp4", "wmv", "mkv", "flv",
                        "wav", "mp3", "wma", "aac", "flac"};
                if (flag == 1) {
                    for (int j = 0; j < doc.length; j++) {
                        if (suffix.equals(doc[j])) {
                            ListMap.add(list);
                            break;
                        }
                    }
                }
                if (flag == 2) {
                    for (int j = 0; j < pict.length; j++) {
                        if (suffix.equals(pict[j])) {
                            ListMap.add(list);
                            break;
                        }
                    }
                }
                if (flag == 3) {
                    for (int j = 0; j < video.length; j++) {
                        if (suffix.equals(video[j])) {
                            ListMap.add(list);
                            break;
                        }
                    }
                }
                if (flag == 4) {
                    for (int j = 0; j < music.length; j++) {
                        if (suffix.equals(music[j])) {
                            ListMap.add(list);
                            break;
                        }
                    }
                }
                if (flag == 5) {
                    int k = 0;
                    for (int j = 0; j < other.length; j++) {

                        if (!suffix.equals(other[j])) {
                            k++;
                        }
                    }
                    if (k == other.length) {
                        ListMap.add(list);
                    }

                }
            }
        }
    }
}
