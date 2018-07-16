package com.example.cloudstore.repository;

import com.example.cloudstore.domain.Md5;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author jitdc
 * @Date Create in 16:58 2018/7/6
 * @Description:
 */
public interface Md5Repository extends JpaRepository<Md5,Integer> {

    List<Md5> findByFileMd5AndFileName(String fileMd5, String fileName);
    Md5 findByFileMd5AndPathAndFileName (String fileMd5, String path,String fileName);
    List<Md5> findByFileMd5AndPath(String fileMd5,String path);
    List<Md5> findByFileMd5(String fileMd5);
    int  deleteByPathAndFileName (String path ,String filename);

}
