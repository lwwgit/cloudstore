package com.example.cloudstore.controller;

import com.example.cloudstore.domain.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class DownloadController {
    @Value("${HDFS_PATH}")
    private String HDFS_PATH;

    @Value("${DA_PATH}")
    private String dataLocalName;

    @GetMapping("/download")
    public String downloadFile(HttpServletResponse response, String srcName) throws  URISyntaxException,UnsupportedEncodingException{
        String fileName = srcName.substring(srcName.lastIndexOf("/") + 1);
        System.out.println(fileName);
        String userFatherFileName = srcName.split("/")[1];
        downloadHDFS(srcName,dataLocalName+userFatherFileName+"\\"+fileName);

        if (fileName != null) {
            File file = new File(dataLocalName+userFatherFileName+"\\"+fileName);
            System.out.println("file_name:"+ dataLocalName+userFatherFileName+"\\"+fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                String File_name = new String(fileName .getBytes(), "ISO-8859-1");
                System.out.println("下载名字："+File_name);
                response.addHeader("Content-Disposition", "attachment;fileName=\"" + File_name+ "\"");// 设置文件名
                byte[] buffer = new byte[1024 * 64];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    file.delete();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            file.delete();
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    @ApiOperation(value = "下载文件", notes = "文件全路径")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "srcName", value = "下载文件的dfs全路径", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "localName", value = "下载文件到本地的本地全路径", required = true, dataType = "String", paramType = "query"),
    })
    public JsonResult downloadHDFS(@RequestParam String srcName, @RequestParam String localName) throws URISyntaxException {
        JsonResult result = new JsonResult();
        Configuration conf = new Configuration();
        Path downloadPath = new Path(srcName);
        URI uri = new URI(HDFS_PATH);
        FileSystem fileSystem;
        try {
            fileSystem = FileSystem.get(uri, conf);
            if (!fileSystem.exists(downloadPath)) {
                result.setStatus("文件不存在！");
            } else {
                FSDataInputStream HDFS_IN = fileSystem.open(downloadPath);
                OutputStream OutToLOCAL = new FileOutputStream(localName);
                IOUtils.copyBytes(HDFS_IN, OutToLOCAL, 1024, true);
                result.setStatus("下载成功");
                HDFS_IN.close();
                OutToLOCAL.close();
            }
            result.setResult(srcName.substring(srcName.lastIndexOf("/") + 1));
        } catch (IOException e) {
            e.printStackTrace();
            result.setResult(e.getMessage());
            result.setStatus("下载失败！");
        }
        System.out.println(result.getStatus() + ": " + result.getResult());
        return result;
    }


}
