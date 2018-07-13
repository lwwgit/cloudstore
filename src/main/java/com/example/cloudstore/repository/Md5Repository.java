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

    Md5 findByFileMd5AndFileName(String fileMd5, String fileName);

}
