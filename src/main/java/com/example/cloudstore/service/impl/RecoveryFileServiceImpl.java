package com.example.cloudstore.service.impl;


import com.example.cloudstore.controller.GlobalFunction;
import com.example.cloudstore.domain.Constants;
import com.example.cloudstore.domain.Md5;
import com.example.cloudstore.domain.entity.RecoveryFile;
import com.example.cloudstore.repository.Md5Repository;
import com.example.cloudstore.repository.RecoveryFileRepository;
import com.example.cloudstore.service.RecoveryFileService;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class RecoveryFileServiceImpl implements RecoveryFileService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GlobalFunction globalFunction;
    @Autowired
    private Md5Repository md5Repository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


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
        String oldname = oriPath.substring(oriPath.lastIndexOf("/") + 1);
        String oldFatherPath = oriPath.substring(0,oriPath.length()-oldname.length()-1);
        String newFatherPath = dstPath.substring(0,dstPath.length()-1);
        /*文件的新hdfs全路径*/
        String newDirPath = dstPath + oldname;
        //先判断当前路径是否是文件夹
        Md5 IsDir = md5Repository.findByUidAndPathAndFileName("文件夹",oldFatherPath,oldname);
        if (IsDir != null){
       //是文件夹，修改文件夹的路径
            int i = md5Repository.updateDirPath(newFatherPath,"文件夹",oldFatherPath,oldname);
            List<Md5> byPathLike = md5Repository.findByPathLike(oriPath);//找出所有在该文件夹下的文件
            for (Md5 md5:byPathLike){
                String newFilePath  = newFatherPath+ md5.getPath().substring(oldFatherPath.length());
                md5Repository.updateFilePathInDirNameLike(newFilePath,md5.getPath());
            }
        }
        else {//是文件，修改该条数据的路径
            int i = md5Repository.updateFilePath(newFatherPath, oldFatherPath, oldname);
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
        String oldname = filePath.substring(filePath.lastIndexOf("/") + 1);
        String oldFatherPath = filePath.substring(0,filePath.length()-oldname.length()-1);
        Md5 IsDir = md5Repository.findByUidAndPathAndFileName("文件夹",oldFatherPath,oldname);
        if (IsDir != null){
       //是文件夹，删除文件夹的数据，再删除在该文件夹下所有的文件
            int i = md5Repository.deleteByPath(filePath);
            md5Repository.deleteByUidAndPathAndFileName("文件夹",oldFatherPath,oldname);
            md5Repository.deleteByPathLike(filePath);
        }
        else {//是文件，删除该条数据的路径
            Md5 byFileNameAndPath = md5Repository.findByFileNameAndPath(oldname, oldFatherPath);
            int i = md5Repository.deleteByPathAndFileName(oldFatherPath,oldname);
            List<Md5> byFileMd5 = md5Repository.findByFileMd5(byFileNameAndPath.getFileMd5());
            if (byFileMd5.size() == 0){
                stringRedisTemplate.opsForHash().delete(Constants.FILE_UPLOAD_STATUS, byFileNameAndPath.getFileMd5());
                System.out.println("删除的key值："+Constants.FILE_MD5_KEY+byFileNameAndPath.getFileMd5());
                stringRedisTemplate.delete(Constants.FILE_MD5_KEY+byFileNameAndPath.getFileMd5());
            }
        }
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
}
