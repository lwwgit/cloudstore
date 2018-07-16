package com.example.cloudstore.service.impl;

import com.example.cloudstore.domain.Md5;
import com.example.cloudstore.repository.Md5Repository;
import com.example.cloudstore.service.Md5service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author jitdc
 * @Date Create in 16:35 2018/7/12
 * @Description:
 */
@Service
public class Md5serviceImpl implements Md5service {
    @Autowired
    private Md5Repository md5Repository;

    @Override
    public Md5 save(Md5 md5) {
        return md5Repository.save(md5);
    }

    @Override
    public Md5 findByMd5AndPathAndFilename(String md5, String path, String filename) {
        Md5 file = md5Repository.findByFileMd5AndPathAndFileName(md5, path, filename);
        return file;
    }

    @Override
    public List<Md5> findByMd5AndFilename(String md5, String filename) {
        List<Md5> results = md5Repository.findByFileMd5AndFileName(md5, filename);
        return results;
    }

    @Override
    public List<Md5> findByMd5AndPath(String md5, String path) {
        List<Md5> byFileMd5AndPath = md5Repository.findByFileMd5AndPath(md5, path);
        return byFileMd5AndPath;
    }

    @Override
    public List<Md5> findByFileMd5(String md5) {
        List<Md5> byFileMd5 = md5Repository.findByFileMd5(md5);
        return byFileMd5;
    }

}
