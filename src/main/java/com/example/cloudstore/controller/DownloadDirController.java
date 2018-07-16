package com.example.cloudstore.controller;



import com.example.cloudstore.utils.HDFS_Downloader;
import com.example.cloudstore.utils.ZipUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;

@RestController
public class DownloadDirController {

    private String dataLocalName = "D:\\IDEA\\project\\cloudstore\\src\\main\\java\\com\\example\\cloudstore\\data\\";

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

    @GetMapping("/downloadBatch")
    @ApiOperation(value = "批量下载文件或者文件夹", notes = "")
    public String downloadFileBatch(HttpServletResponse response, @RequestParam String paths) {
        String[] srcnames = paths.split(",");
        /*创建个人临时文件夹*/
        String fileName = "batch";
        String userFatherFileName = srcnames[0].split("/")[1];
        String localFileName = dataLocalName + userFatherFileName + "\\" + fileName;
        File dstDir = new File(localFileName);
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }
        /*将要下载的文件先放到个人临时文件夹里*/
        for(String srcName : srcnames) {
            String filename = srcName.substring(srcName.lastIndexOf("/") + 1);
            System.out.println(filename);
            String localfilepath = localFileName + "\\" + filename;
            try {
                HDFS_Downloader.download(srcName, localfilepath);
            } catch (Exception e){
                e.printStackTrace();
                return "下载hdfs文件异常";
            }
        }
            /*将该文件夹打包zip*/
        try{
            ZipUtils.doCompress(localFileName, localFileName + ".zip");
        }catch (IOException e) {
            e.printStackTrace();
            return "下载错误：" + e.getMessage();
        }
            /*打包结束后，删除该包内容*/
        File orginalFile = new File(localFileName);
        deleteFile(orginalFile);
            /*向前端发送下载链接*/
        if (fileName != null) {
            File file = new File(localFileName + ".zip");
            try {
                String FileName = fileName+".zip";
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String(FileName.getBytes("ISO8859-1"), "UTF-8"));
                response.setContentType("multipart/form-data");// 定义输出类型
                response.setContentLength((int) file.length());
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