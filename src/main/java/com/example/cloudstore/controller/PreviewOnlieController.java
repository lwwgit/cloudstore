package com.example.cloudstore.controller;


import com.example.cloudstore.domain.JsonResult;
import com.example.cloudstore.utils.ExcelToPdf;
import com.example.cloudstore.utils.PptToPdf;
import com.example.cloudstore.utils.TxtToPdf;
import com.example.cloudstore.utils.WordToPdf;
import com.itextpdf.text.DocumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@RestController
public class PreviewOnlieController {

    @Value("${HDFS_PATH}")
    private String HDFS_PATH;

    @Value("${PC_PATH}")
    private String PC_PATH;
    
     private String localName ="E:\\IDEA_FILE\\zhuoyun\\cloudstore\\src\\main\\java\\com\\example\\cloudstore\\temp\\";
     private String outputName = "E:\\IDEA_FILE\\zhuoyun\\cloudstore\\src\\main\\java\\com\\example\\cloudstore\\temp\\";

    @PostMapping("/file2Pdf")
    public JsonResult file2Pdf(@RequestParam String input) throws  URISyntaxException,DocumentException{
        JsonResult jsonResult  = new JsonResult();
        String fileName = input.substring(input.lastIndexOf("/")+1);
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        System.out.println("fileName : " + fileName);
        System.out.println("extension : " + extension);
        if( extension.equals("ppt")|| extension.equals("pptx")){
            return ppt2Pdf(input);
        }
        if (extension.equals("doc")|| extension.equals("docx")){
            return word2Pdf(input);
        }
        if (extension.equals("xls")|| extension.equals("xlsx")){
            return excel2Pdf(input);
        }
        if (extension.equals("c")|| extension.equals("java")|| extension.equals("txt")|| extension.equals("py")||extension.equals("cpp")){
            return jc2Pdf(input);
        }
        jsonResult.setStatus("转换失败");
        jsonResult.setResult("格式无法转换");
        return jsonResult;
    }

    public JsonResult word2Pdf(@RequestParam String input) throws URISyntaxException {
        JsonResult result = new JsonResult();
        Configuration conf = new Configuration();
        URI uri = new URI(HDFS_PATH);
        FileSystem fileSystem;
        try{
            fileSystem = FileSystem.get(uri, conf);
            Path inputPath = new Path(input);
            if (!fileSystem.exists(inputPath)) {
                 result.setStatus("文件不存在");
            } else {
                FSDataInputStream fileInput = fileSystem.open(inputPath);
                Date date = new Date();
                Long time = date.getTime();
                int i = WordToPdf.run(fileInput,outputName+time+".pdf");
                switch (i) {
                    default:
                        result.setStatus("成功！耗时：" + i);
                        result.setResult(PC_PATH + "/upload/"+time+".pdf");
                }
                fileInput.close();
            }
        }catch (IOException e){
            e.printStackTrace();
            result.setResult(e.getMessage());
            result.setStatus("下载失败！");
        }

        return result;
    }

    public JsonResult excel2Pdf(@RequestParam String input) throws URISyntaxException {
        JsonResult result = new JsonResult();
        Configuration conf = new Configuration();
        URI uri = new URI(HDFS_PATH);
        FileSystem fileSystem;
        try{
            fileSystem = FileSystem.get(uri, conf);
            Path inputPath = new Path(input);
            if (!fileSystem.exists(inputPath)) {
                result.setStatus("文件不存在");
            }else {
                FSDataInputStream HDFS_IN = fileSystem.open(inputPath);
                OutputStream OutToLOCAL = new FileOutputStream(localName + input.substring(input.lastIndexOf("/") + 1));
                IOUtils.copyBytes(HDFS_IN, OutToLOCAL, 1024, true);
                String fileName = input.substring(input.lastIndexOf("/") + 1);
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1);
                System.out.println(fileSuffix);
                Date date = new Date();
                Long time = date.getTime();
                String localNamePath = localName+input.substring(input.lastIndexOf("/") + 1); //文件后缀名
                int i = ExcelToPdf.run(localNamePath,outputName+time+".pdf");
                File file = new File(localNamePath);
                file.delete();//删除文件
                switch (i) {
                    default:
                        result.setStatus("成功！耗时：" + i);
                        result.setResult(PC_PATH + "/upload/"+time+".pdf");
                }
                HDFS_IN.close();
                OutToLOCAL.close();
            }
        }catch (IOException e){
            e.printStackTrace();
            result.setResult(e.getMessage());
            result.setStatus("下载失败！");
        }
        return result;
    }

    public JsonResult ppt2Pdf(@RequestParam String input) throws URISyntaxException {
        //String input = fileService.findFile(id).getFilepath();

        JsonResult result = new JsonResult();
        Configuration conf = new Configuration();
        URI uri = new URI(HDFS_PATH);
        FileSystem fileSystem;
        try{
            fileSystem = FileSystem.get(uri, conf);
            Path inputPath = new Path(input);
            if (!fileSystem.exists(inputPath)) {
                result.setStatus("文件不存在");
            }else {
                FSDataInputStream HDFS_IN = fileSystem.open(inputPath);
                OutputStream OutToLOCAL = new FileOutputStream(localName + input.substring(input.lastIndexOf("/") + 1));
                IOUtils.copyBytes(HDFS_IN, OutToLOCAL, 1024, true);
                String fileName = input.substring(input.lastIndexOf("/") + 1);
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1);
                System.out.println(fileSuffix);
                Date date = new Date();
                Long time = date.getTime();
                String localNamePath = localName+input.substring(input.lastIndexOf("/") + 1);
                int i = PptToPdf.run(localNamePath,outputName+time+".pdf");
                File file = new File(localNamePath);
                file.delete();//删除文件
                switch (i) {
                    default:
                        result.setStatus("成功！耗时：" + i);
                        result.setResult(PC_PATH + "/upload/"+time+".pdf");
                }
                HDFS_IN.close();
                OutToLOCAL.close();
            }
        }catch (IOException e){
            e.printStackTrace();
            result.setResult(e.getMessage());
            result.setStatus("下载失败！");
        }
        return result;
    }

    public JsonResult jc2Pdf(@RequestParam String input) throws URISyntaxException,DocumentException {
        String name = input.substring(input.lastIndexOf("/")+1);//文件名
        System.out.println(name);
        String Name = name.substring(0,name.indexOf(".")); //去掉后缀名的文件ming
        System.out.println(Name);
        JsonResult result = new JsonResult();
        Configuration conf = new Configuration();
        URI uri = new URI(HDFS_PATH);
        FileSystem fileSystem;
        try{
            Path inputPath = new Path(input);
            fileSystem = FileSystem.get(uri, conf);
            if (!fileSystem.exists(inputPath)) {
                result.setStatus("文件不存在");
            }else {
                FSDataInputStream HDFS_IN = fileSystem.open(inputPath);
                OutputStream OutToLOCAL = new FileOutputStream(localName + input.substring(input.lastIndexOf("/") + 1));
                IOUtils.copyBytes(HDFS_IN, OutToLOCAL, 1024, true);
                File file = new File(localName+input.substring(input.lastIndexOf("/") + 1));
                String newName = Name + ".txt";
                File newfile = new File(localName+newName);
                file.renameTo(newfile);
                String fileSuffix = newName.substring(newName.lastIndexOf(".")+1);
                System.out.println(fileSuffix);

                Date date = new Date();
                Long time = date.getTime();
                String localNamePath = localName+newName;
                TxtToPdf.txt2pdf(localNamePath,outputName+time+".pdf");
                File file1 = new File(localNamePath);
                file1.delete();//删除文件
                result.setStatus("预览成功");
                result.setResult(PC_PATH + "/upload/"+time+".pdf");
                HDFS_IN.close();
                OutToLOCAL.close();
            }
        }catch (IOException e){
            e.printStackTrace();
            result.setResult(e.getMessage());
            result.setStatus("下载失败！");
        }
        return result;
    }

    //取消预览之后，调用此接口
    @PostMapping("/pdfDelete")
    public JsonResult pdfDelete(@RequestParam String deletePath) {
        String Name = deletePath.substring(deletePath.lastIndexOf("/")+1);
        String Path = localName + Name;
        JsonResult result = new JsonResult();
        File file = new File(Path);
        boolean i = file.delete();//删除文件
        if(i==true){
            result.setStatus("删除成功");
            result.setResult(deletePath);
        }else {
            result.setStatus("删除失败");
            result.setResult(deletePath);
        }
        return result;
    }


}
