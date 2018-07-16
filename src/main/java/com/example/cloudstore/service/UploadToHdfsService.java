package com.example.cloudstore.service;

/**
 * @Author jitdc
 * @Date Create in 17:13 2018/7/11
 * @Description:
 */
public interface UploadToHdfsService {
    void uploadHdfs(String src,String dst)throws Exception;
}
