package com.example.cloudstore.service;

import com.example.cloudstore.domain.Md5;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.util.List;

/**
 * @Author jitdc
 * @Date Create in 16:34 2018/7/12
 * @Description:
 */
public interface Md5service {
    Md5 save(Md5 md5);
    Md5 findByMd5AndPathAndFilename(String md5,String path,String filename);
    List<Md5> findByMd5AndFilename(String md5, String filename);
    List<Md5> findByMd5AndPath(String md5,String path);
    List<Md5> findByFileMd5(String fileMd5);
    int  deleteByPathAndFileName (String path ,String filename);
}
