package com.example.cloudstore.service;



import com.example.cloudstore.domain.MultipartFileParam;

import java.io.File;
import java.io.IOException;

/**
 * @Author jitdc
 * @Date Create in 17:28 2018/7/5
 * @Description:
 */
public interface MyStorageService {
    /**
     * 删除全部数据
     */
    void deleteAll();

    /**
     * 初始化方法
     */
    void init();

    /**
     * 上传文件方法1
     *
     * @param param
     * @throws IOException
     */
    void uploadFileRandomAccessFile(MultipartFileParam param) throws IOException;

    /**
     * 上传文件方法2
     * 处理文件分块，基于MappedByteBuffer来实现文件的保存
     *
     * @param param
     * @throws IOException
     */
    void uploadFileByMappedByteBuffer(MultipartFileParam param) throws Exception;

    boolean deleteFile(String sPath);
    boolean deleteDirectory(String sPath);
}
