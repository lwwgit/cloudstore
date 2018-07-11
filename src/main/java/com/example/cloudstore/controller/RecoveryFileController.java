package com.example.cloudstore.controller;


import com.example.cloudstore.domain.Result;
import com.example.cloudstore.domain.entity.RecoveryFile;
import com.example.cloudstore.service.CopyFileService;
import com.example.cloudstore.service.RecoveryFileService;
import com.example.cloudstore.utils.ResultUtil;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
public class RecoveryFileController {
    @Autowired
    private RecoveryFileService recoveryFileService;
    @Autowired
    private GlobalFunction globalFunction;
    @Autowired
    private CopyFileService copyFileService;



    /**
     * 获取回收站所有文件
     */
    @GetMapping("/user/recycle/get")
    public Result getRecycleFile(){
        String username = globalFunction.getUsername();
        List<RecoveryFile> recoveryFiles = recoveryFileService.findByUsername(username);
        return ResultUtil.success(recoveryFiles);
    }

    /**
     * 文件单个删除进回收站
     * @param oriPath
     * @return
     * @throws IOException
     */
//    @PostMapping("/user/recycle/insert")
//    public Result moveToRecycleBin(String oriPath) throws IOException {
//        String username = globalFunction.getUsername();
//        String dstPath = "/" + username + "tmp/";//该用户回收站目录
//
//        recoveryFileService.MoveToRecovery(oriPath,dstPath);
//        //将文件或目录源路径插入数据库
//        RecoveryFile recoveryFile = new RecoveryFile();
//
//        if(oriPath.lastIndexOf(".") == -1){
//            recoveryFile.setType("folder");
//        }else{
//            String suffix = oriPath.substring(oriPath.lastIndexOf(".") + 1);//文件后缀名
//            String type = globalFunction.getFileType(suffix);
//            recoveryFile.setType(type);
//        }
//        String filename = oriPath.substring(oriPath.lastIndexOf("/") + 1);
//        Path path = new Path(dstPath + filename);
//        FileStatus[] files = globalFunction.getHadoopFileSystem().listStatus(path);
//        for (int i = 0; i < files.length; i++) {
//            recoveryFile.setSize(files[i].getLen());
//        }
//        recoveryFile.setPresentPath(dstPath + filename);
//        recoveryFile.setOriginalPath(oriPath);
//        recoveryFile.setUsername(username);
//        recoveryFile.setDelTime(new Date());
//        recoveryFile.setFileName(filename);
//        recoveryFileService.insert(recoveryFile);
//        return ResultUtil.success();
//    }

    /**
     * 文件批量删除进回收站
     * Path oldPath = new Path("/hyw/test/huabingood");
     * Path newPath = new Path("/hyw/test/huabing");
     */
    @PostMapping("/user/recycle/insert/all")
    public Result moveToRecycleBin(String[] oriPaths) throws IOException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String username = globalFunction.getUsername();
        String dstPath = "/" + username + "tmp/";//该用户回收站目录
        for(String oriPath:oriPaths){
            recoveryFileService.MoveToRecovery(oriPath,dstPath);

            //将文件或目录源路径插入数据库
            RecoveryFile recoveryFile = new RecoveryFile();
            String filename = oriPath.substring(oriPath.lastIndexOf("/") + 1);
            if(oriPath.lastIndexOf(".") == -1){
                recoveryFile.setType("folder");
            }else{
                String suffixName = oriPath.substring(oriPath.lastIndexOf(".") + 1);
                String fileType = globalFunction.getFileType(suffixName);
                recoveryFile.setType(fileType);
            }

            Path path = new Path(dstPath + filename);
            FileStatus file = globalFunction.getHadoopFileSystem().getFileStatus(path);
            recoveryFile.setLen(file.getLen());
            recoveryFile.setDelTime(formatter.format(file.getModificationTime()));
            recoveryFile.setSize(globalFunction.getFileSize(file.getLen()));

            recoveryFile.setOriginalPath(oriPath);
            recoveryFile.setUsername(username);
            recoveryFile.setPresentPath(dstPath + filename);
            recoveryFile.setFileName(filename);
            recoveryFileService.insert(recoveryFile);
        }
        return ResultUtil.success();
    }

    /**
     * 回收站单个文件还原
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/user/recycle/restore")
    public Result restoreRecycleFile(Long id) throws IOException {
        RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(id);
        String presentPath  = recoveryFile.getPresentPath();
        String originalPath = recoveryFile.getOriginalPath();
        if(copyFileService.checkIsExist(originalPath) == false) {
            recoveryFileService.MoveToRecovery(presentPath, originalPath);
            recoveryFileService.deleteRecoveryFile(id);
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"还原失败，文件（夹）已存在");
        }
    }

    /**
     * 回收站多个文件还原
     * @param ids
     * @return
     * @throws IOException
     */
    @PostMapping("/user/recycle/restore/all")
    public Result restoreRecycleFile(Long[] ids) throws IOException {
        for(Long checkId : ids) {
            RecoveryFile checkRecoveryFile = recoveryFileService.findByRecoveryId(checkId);
            String checkOriginalPath = checkRecoveryFile.getOriginalPath();
            if(copyFileService.checkIsExist(checkOriginalPath) == true) {
                return ResultUtil.error(1,"还原失败，文件（夹）已存在");
            }
        }
        for(Long id : ids) {
            RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(id);
            String presentPath = recoveryFile.getPresentPath();
            String originalPath = recoveryFile.getOriginalPath();
            recoveryFileService.MoveToRecovery(presentPath, originalPath);
            recoveryFileService.deleteRecoveryFile(id);
        }
        return ResultUtil.success();
    }


    /**
     * 回收站删除文件或目录(单个)
     * @return
     * @throws IOException
     */
    @PostMapping("/user/recycle/delete")
    public Result deleteRecycleFile(Long id) throws IOException {
        RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(id);
        String presentPath = recoveryFile.getPresentPath();
        boolean b = recoveryFileService.deleteFile(presentPath);
        recoveryFileService.deleteRecoveryFile(id);
        if (b == true){
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"删除失败");
        }
    }

    /**
     * 删除多个文件
     * @return
     * @throws IOException
     */
    @PostMapping("/user/recycle/delete/all")
    public Result deleteRecycleFile(Long[] ids) throws IOException {
        for(Long id : ids) {
            RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(id);
            String presentPath = recoveryFile.getPresentPath();
            recoveryFileService.deleteFile(presentPath);
            recoveryFileService.deleteRecoveryFile(id);
        }
        return ResultUtil.success();
    }


    /**
     * 清空回收站
     * @return
     * @throws IOException
     */
    @PostMapping("/user/delete/all")
    public Result deleteAllRecycleFile() throws IOException {
        String username = globalFunction.getUsername();
        List<RecoveryFile> recoveryFiles = recoveryFileService.findByUsername(username);
        for(int i=0; i<recoveryFiles.size(); i++){
            String presentPath = recoveryFiles.get(i).getPresentPath();
            recoveryFileService.deleteFile(presentPath);
            recoveryFileService.deleteRecoveryFile(recoveryFiles.get(i).getRecoveryId());
        }
        return ResultUtil.success();
    }

}
