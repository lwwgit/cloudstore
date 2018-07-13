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
import com.example.cloudstore.service.Md5service;
import com.example.cloudstore.service.MyStorageService;
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

/**
 * 断点续传上传大文件类
 */
@RestController
@RequestMapping(value = "/break")
public class BreakPointController {

    private Logger logger = LoggerFactory.getLogger(BreakPointController.class);

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
        if (processingObj == null) { //判断文件是否存在 该文件没有上传过
            Md5 md51 = new Md5();
            md51.setUid(myMd5.getUid());
            md51.setFileName(myMd5.getFileName());
            md51.setFileMd5(myMd5.getFileMd5());
            md51.setPath(myMd5.getPath());
            md51.setCreateTime(new Date());
            md5service.save(md51);
            return new ResultVo(ResultStatus.NO_HAVE);
        }
        String processingStr = processingObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = stringRedisTemplate.opsForValue().get(Constants.FILE_MD5_KEY + myMd5.getFileMd5());
        if (processing) {//该文件已经上传过了
            return new ResultVo(ResultStatus.IS_HAVE, value);
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