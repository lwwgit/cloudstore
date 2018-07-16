package com.example.cloudstore.controller;

/**
 * @Author jitdc
 * @Date Create in 17:24 2018/7/5
 * @Description:
 */


import com.example.cloudstore.domain.Constants;
import com.example.cloudstore.domain.Md5;
import com.example.cloudstore.domain.MultipartFileParam;
import com.example.cloudstore.enums.ResultStatus;
import com.example.cloudstore.domain.ResultVo;
import com.example.cloudstore.service.CopyFileService;
import com.example.cloudstore.service.Md5service;
import com.example.cloudstore.service.MyStorageService;
import com.example.cloudstore.utils.ResultUtil;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 断点续传上传大文件类
 */
@RestController
@RequestMapping(value = "/break")
public class BreakPointController {

    private Logger logger = LoggerFactory.getLogger(BreakPointController.class);

    @Autowired
    private CopyFileService copyFileService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MyStorageService myStorageService;
    @Autowired
    private Md5service md5service;
    /**
     * 秒传判断，断点判断
     *
     * @return
     */
    @RequestMapping(value = "checkFileMd5")
    @ResponseBody
    public Object checkFileMd5(Md5 myMd5) throws IOException {
       //  myStorageService.deleteAll();
      // stringRedisTemplate.opsForHash().delete(Constants.FILE_UPLOAD_STATUS, myMd5.getFileMd5());
        Object processingObj = stringRedisTemplate.opsForHash().get(Constants.FILE_UPLOAD_STATUS, myMd5.getFileMd5());
        Md5 byMd5AndPathAndFilename = md5service.findByMd5AndPathAndFilename(myMd5.getFileMd5(), myMd5.getPath(), myMd5.getFileName());
        if (processingObj == null) { //判断文件是否存在 该文件没有上传过
            if (byMd5AndPathAndFilename == null){
                Md5 md51 = new Md5();
                md51.setUid(myMd5.getUid());
                md51.setFileName(myMd5.getFileName());
                md51.setFileMd5(myMd5.getFileMd5());
                md51.setPath(myMd5.getPath());
                md51.setCreateTime(new Date());
                md5service.save(md51);
            }
            return new ResultVo(ResultStatus.NO_HAVE);
        }
        String processingStr = processingObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = stringRedisTemplate.opsForValue().get(Constants.FILE_MD5_KEY + myMd5.getFileMd5());
        if (processing) {//该文件已经上传过了 —— 下面所有的功能都是秒传
            //先判断这次上传的文件在当前路径下是否存在
            List<Md5> byMd5AndPath = md5service.findByMd5AndPath(myMd5.getFileMd5(), myMd5.getPath());
            List<Md5> byMd5AndFilename = md5service.findByMd5AndFilename(myMd5.getFileMd5(), myMd5.getFileName());
            //从文件系统中找到一个Md5值相同的文件
            List<Md5> byFileMd5 = md5service.findByFileMd5(myMd5.getFileMd5());
            Md5 tmpMd5 = new Md5();
            for (Md5 md5:byFileMd5){
                tmpMd5.setPath(md5.getPath());
                tmpMd5.setFileName(md5.getFileName());
                break;
            }
            if (byMd5AndPath == null){ //这次想要上传的文件在当前路径下没有 —— 不同的路径上传
                //将找到的一个md5值相同的文件复制到当前路径下
                boolean b = copyFileService.copyFile(tmpMd5.getPath() + "/" + tmpMd5.getFileName(), myMd5.getPath());
                // 同一个文件，文件名修改了，内容没变
                // 文件名没有修改，从文件系统中复制一个md5值相同的文件到当前路径下
                if (byMd5AndFilename == null){ // 同一个文件，文件名修改了，内容没变
                    //先从文件系统中复制一个md5值相同的文件到当前路径下，然后重命名文件为myMd5.getFileName()
                    if (b){
                       //如果复制成功了，再调用重命名方法。然后删除数据库中由于复制而多出来的一条数据
                        //先重命名
                        int i = md5service.deleteByPathAndFileName(myMd5.getPath(), tmpMd5.getFileName());
                    }
                    else
                        return new ResultVo(ResultStatus.IS_ERROR, value);
                }
                if (b)
                    return new ResultVo(ResultStatus.IS_HAVE, value);
                else
                    return new ResultVo(ResultStatus.IS_ERROR, value);
            }
            else { //这次想要上传的文件在当前路径下存在 —— 同一路径上传
                if (byMd5AndPathAndFilename== null){ // 同一个文件，文件名修改了，内容没变
                    //同一个路径直接复制会覆盖原本文件，所以再新建一个临时文件，保存复制过来的文件

                    //创建临时文件夹
                    Integer tmp;
                    String tempDirPath = myMd5.getPath()+"/";
                    for (int i=0;i<4;i++) {
                        tmp = new Random().nextInt(10);
                        tempDirPath = tempDirPath+String.valueOf(tmp);
                    }
                    File tmpDir = new File(tempDirPath);
                    if (!tmpDir.exists()) {
                        tmpDir.mkdirs();
                    }
                    else
                        System.out.println("临时文件夹已存在，创建失败");
                    boolean b = copyFileService.copyFile(tmpMd5.getPath() + "/" + tmpMd5.getFileName(), tempDirPath);
                    //下面重命名文件

                    //删除数据库中数据

                    //再移动到当前上传的路径下

                    //删除临时文件夹
                    myStorageService.deleteDirectory(tempDirPath);

                    return new ResultVo(ResultStatus.IS_HAVE, value);
                }
                else {//文件名没有修改,什么都不做
                    return new ResultVo(ResultStatus.IS_HAVE, value);
                }
            }

        } else {
            File confFile = new File(value);
            byte[] completeList = FileUtils.readFileToByteArray(confFile);
            List<String> missChunkList = new LinkedList<>();
            for (int i = 0; i < completeList.length; i++) {
                if (completeList[i] != Byte.MAX_VALUE) {
                    missChunkList.add(i + "");
                }
            }
            return new ResultVo<>(ResultStatus.ING_HAVE, missChunkList);
        }
    }

    /**
     * 上传文件
     *
     * @param param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fileUpload")
    @ResponseBody
    public ResponseEntity MyCosFileUpload(MultipartFileParam param, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("文件的md5值是："+param.getMd5());
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            logger.info("上传文件start。");
            try {
                // 方法1
                //myStorageService.uploadFileRandomAccessFile(param);
                // 方法2 这个更快点
                myStorageService.uploadFileByMappedByteBuffer(param);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("文件上传失败。{}", param.toString());
            }
            logger.info("上传文件end。");
        }
        return ResponseEntity.ok().body("上传成功。");
    }

}