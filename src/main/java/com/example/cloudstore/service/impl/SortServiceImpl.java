package com.example.cloudstore.service.impl;

import com.aspose.slides.p2cbca448.ong;
import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.entity.UserStore;
import com.example.cloudstore.repository.UserStoreRepository;
import com.example.cloudstore.service.SortService;
import org.apache.commons.lang3.StringUtils;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SortServiceImpl implements SortService {

    @Autowired
    UserStoreRepository userStoreRepository;

    @Value("${HDFS_PATH}")
    private String HdfsPath;
    //更改文件服务器地址
//    static String HdfsPath = "hdfs://maste:9000";

    @Override
    public List<Map<String, Object>> SortFile(int flag) throws URISyntaxException, IOException {

        /**************  确定用户文件夹  *******************/
        GlobalFunction globalFunction = new GlobalFunction();
        String name = globalFunction.getUsername();
        /*************************************************/
        FileSystem hdfs = null;
        Configuration config = new Configuration();

        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);
        Path path = new Path("/" + name);/**********确定用户文件夹  new Path("/" + name)***********/
        List<Map<String, Object>> ListMap = new ArrayList<>();

        //文件分类
        String[] doc = new String[]{"docx", "doc", "xlsx", "xls", "pptx", "ppt", "txt", "pdf", "c"};
        String[] pict = new String[]{"jpg", "png", "gif", "jpeg", "bmp", "JPG"};
        String[] video = new String[]{"avi", "mov", "mp4", "wmv", "mkv", "flv"};
        String[] music = new String[]{"wav", "mp3", "wma", "aac", "flac", "ram", "m4a"};
        String[] other = new String[]{
                "docx", "doc", "xlsx", "xls", "pptx", "ppt", "txt", "pdf",
                "jpg", "png", "gif", "jpeg", "JPG",
                "avi", "mov", "mp4", "wmv", "mkv", "flv",
                "wav", "mp3", "wma", "aac", "flac"};

        ShowFile(hdfs, path, ListMap, flag, doc, pict, video, music, other);
        return ListMap;
    }

    public void ShowFile(FileSystem hdfs, Path path,
                         List<Map<String, Object>> ListMap, int flag,
                         String[] doc, String[] pict, String[] video,
                         String[] music, String[] other) throws IOException {

        GlobalFunction globalFunction = new GlobalFunction();

        FileStatus[] files = hdfs.listStatus(path);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //展示文件信息
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                //递归调用
                ShowFile(hdfs, files[i].getPath(), ListMap, flag, doc, pict, video, music, other);

            }
            if (files[i].isFile()) {

                String name = files[i].getPath().getName();
                String suffix = name.substring(name.lastIndexOf(".") + 1);

                Map<String, Object> list = new HashMap<>();
                list.put("fileName", files[i].getPath().getName());

                String truePath = files[i].getPath().toString().substring(files[i].getPath().toString().indexOf("9000") + 4);
                list.put("path", truePath);
                list.put("time", formatter.format(files[i].getModificationTime()));
                list.put("size", globalFunction.getFileSize(files[i].getLen()));
                list.put("length", files[i].getLen());
                list.put("type", globalFunction.getFileType(suffix));

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

    @Override
    public List<Map<String, Object>> SortCapacity() throws IOException, URISyntaxException {

        GlobalFunction globalFunction = new GlobalFunction();

        /*** 测试时要修改username ***/
            String username = globalFunction.getUsername();
//        String username = "lww";

        UserStore userStore = userStoreRepository.findByUsername(username);
        String availableCapacity = userStore.getAvailableCapacity();

        //设置百分比计算时需要保留的小数位
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2); //保留四位小数

        long totalLength = 0;
        List<Map<String, Object>> returnList = new ArrayList<>();
        for (int flag = 1; flag <= 5; flag++) {
            Map<String, Object> map = new HashMap<>();
            if (flag == 1) {
                map.put("type", "doc");
            }
            if (flag == 2) {
                map.put("type", "pict");
            }
            if (flag == 3) {
                map.put("type", "video");
            }
            if (flag == 4) {
                map.put("type", "music");
            }
            if (flag == 5) {
                map.put("type", "other");
            }

            //根据flag循环遍历不同类型的文件，生成MapList
            List<Map<String, Object>> list = SortFile(flag);

            //获取length, size, availableCapacity这些变量
            long length = 0;
            for (int i = 0; i < list.size(); i++) {
                length += Long.valueOf(list.get(i).get("length").toString());
            }
            totalLength += length;
            String size = globalFunction.getFileSize(length);

            //计算该类文件占所有用户文件容量的百分比
            long numOnly = Long.valueOf(StringUtils.substringBefore(availableCapacity, "G"));
            float num1 = (float)length;
            float scale = Float.valueOf(numberFormat.format(num1 / (numOnly * 1024 * 1024 * 1024)*100));

            //添加数据，生成该类型文件的map，并添加到returnList中
            map.put("length", length);
            map.put("size", size);
            map.put("availableCapacity", availableCapacity);
            map.put("scale", scale);

            returnList.add(map);
        }

        //计算和添加所有已用类型的总数据
        String totalSize = globalFunction.getFileSize(totalLength);

        Long numOnly = Long.valueOf(StringUtils.substringBefore(availableCapacity, "G"));
        float num2 =  totalLength;

        Float totalScale = Float.valueOf(numberFormat.format(num2 / (numOnly * 1024 * 1024 * 1024)*100));
        Map<String, Object> map = new HashMap<>();
        map.put("type", "total");
        map.put("length", totalLength);
        map.put("size", totalSize);
        map.put("scale", totalScale);
        map.put("availableCapacity", availableCapacity);
        returnList.add(map);

        //计算每个类型所占空间占所有已用空间的比例
        for (int j = 0; j < returnList.size(); j ++){

            float n1 = Long.valueOf(returnList.get(j).get("length").toString());
            float laterScale = Float.valueOf(numberFormat.format(n1/totalLength*100));
            returnList.get(j).put("laterScale", laterScale);
        }

        return returnList;
    }
}
