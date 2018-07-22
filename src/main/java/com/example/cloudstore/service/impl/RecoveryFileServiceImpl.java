package com.example.cloudstore.service.impl;


import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.entity.RecoveryFile;
import com.example.cloudstore.repository.RecoveryFileRepository;
import com.example.cloudstore.service.RecoveryFileService;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RecoveryFileServiceImpl implements RecoveryFileService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GlobalFunction globalFunction;


    @Autowired
    private RecoveryFileRepository recoveryFileRepository;


    public boolean MoveToRecovery(String oriPath,String dstPath) throws IOException{
        FileSystem fs = globalFunction.getHadoopFileSystem();


        boolean b = false;
        Path oldPath = new Path(oriPath);
        Path newPath = new Path(dstPath);

        try {
            b = fs.rename(oldPath,newPath);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        return b;
    }

    @Override
    public boolean deleteFile(String filePath) throws IOException {
        //获取文件系统
        FileSystem fs = globalFunction.getHadoopFileSystem();
        Path path = new Path(filePath);

        //调用deleteOnExit(）
        boolean flag = fs.deleteOnExit(path);
        //	fs.delete(path);
        if(flag) {
            System.out.println("delete ok!");
        }else {
            System.out.println("delete failure");
        }
        //关闭文件系统
        fs.close();
        return  true;
    }

    @Override
    public RecoveryFile insert(RecoveryFile recoveryFile) {
        return recoveryFileRepository.save(recoveryFile);
    }

    @Override
    public void deleteRecoveryFile(Long id) {
        recoveryFileRepository.deleteById(id);
    }

    @Override
    public List<RecoveryFile> findByUsername(String username) {
        return recoveryFileRepository.findByUsername(username);
    }

    @Override
    public RecoveryFile findByRecoveryId(Long recoveryId) {
        return recoveryFileRepository.findByRecoveryId(recoveryId);
    }

    @Override
    public RecoveryFile findByFileName(String fileName) {
        return recoveryFileRepository.findByFileName(fileName);
    }
}
