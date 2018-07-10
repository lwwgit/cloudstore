package com.example.cloudstore.controller;



import com.example.cloudstore.utils.HDFS_Downloader;
import com.example.cloudstore.utils.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

@RestController
public class DownloadDirController {

    private String dataLocalName = "E:\\IDEA_FILE\\zhuoyun\\cloudstore\\src\\main\\java\\com\\example\\cloudstore\\data\\";

    @GetMapping("/downloadFolder")
    public String downloadFile(HttpServletResponse response,@RequestParam String srcName) throws URISyntaxException, Exception {
        System.out.println(srcName);
        String fileName = srcName.substring(srcName.lastIndexOf("/") + 1); //fileName = file
        String userFatherFileName = srcName.split("/")[1];  //userFatherFilename = user
        String localFileName = dataLocalName + userFatherFileName + "\\" + fileName;
        System.out.println(localFileName);
        HDFS_Downloader.download(srcName, localFileName);

        ZipUtils.doCompress(localFileName, localFileName + ".zip");
        File orginalFile = new File(localFileName);
        deleteFile(orginalFile);

        if (fileName != null) {
            File file = new File(localFileName + ".zip");
            try {
                String FileName = fileName+".zip";
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String(FileName.getBytes(), "ISO-8859-1"));
                response.setContentLength((int) file.length());
                response.setContentType("multipart/form-data");// 定义输出类型
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream buff = new BufferedInputStream(fis);
                byte[] b = new byte[1024];// 相当于我们的缓存
                long k = 0;// 该值用于计算当前实际下载了多少字节
                OutputStream myout = response.getOutputStream();// 从response对象中得到输出流,准备下载
                // 开始循环下载
                while (k < file.length()) {
                    int j = buff.read(b, 0, 1024);
                    k += j;
                    myout.write(b, 0, j);
                }
                myout.flush();
                buff.close();
                file.delete();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return null;
    }

    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }
        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {
            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }
        return dirFile.delete();
    }

}