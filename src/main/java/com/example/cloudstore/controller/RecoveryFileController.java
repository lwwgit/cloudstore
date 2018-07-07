package com.example.cloudstore.controller;


import com.example.cloudstore.domain.Result;
import com.example.cloudstore.domain.entity.RecoveryFile;
import com.example.cloudstore.service.RecoveryFileService;
import com.example.cloudstore.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class RecoveryFileController {
    @Autowired
    private RecoveryFileService recoveryFileService;
    @Autowired
    private GlobalFunction globalFunction;

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
    @PostMapping("/user/recycle/insert")
    public Result moveToRecycleBin(String oriPath) throws IOException {
        System.out.println("删除的源文件为 ：" + oriPath);

        String username = globalFunction.getUsername();
        String dstPath = "/" + username + "tmp/";//该用户回收站目录

        recoveryFileService.MoveToRecovery(oriPath,dstPath);
        //将文件或目录源路径插入数据库
        RecoveryFile recoveryFile = new RecoveryFile();

        if(oriPath.lastIndexOf(".") == -1){
            recoveryFile.setType("dir");
        }else{
            String suffix = oriPath.substring(oriPath.lastIndexOf(".") + 1);//文件后缀名
            recoveryFile.setType(suffix);
        }

        recoveryFile.setOriginalPath(oriPath);
        recoveryFile.setUsername(username);
        String filename = oriPath.substring(oriPath.lastIndexOf("/") + 1);
        recoveryFile.setPresentPath(dstPath + filename);
        recoveryFileService.insert(recoveryFile);
        return ResultUtil.success();
    }

    /**
     * 文件批量删除进回收站
     * Path oldPath = new Path("/hyw/test/huabingood");
     * Path newPath = new Path("/hyw/test/huabing");
     */
    @PostMapping("/user/recycle/insert/all")
    public Result moveToRecycleBin(@RequestBody List<String> oriPaths) throws IOException {
        String username = globalFunction.getUsername();
        String dstPath = "/" + username + "tmp/";//该用户回收站目录
        for(int i = 0; i < oriPaths.size(); i++){
            recoveryFileService.MoveToRecovery(oriPaths.get(i),dstPath);
            //将文件或目录源路径插入数据库
            RecoveryFile recoveryFile = new RecoveryFile();
            if(oriPaths.get(i).lastIndexOf(".") == -1){
                recoveryFile.setType("dir");
            }else{
                String suffix = oriPaths.get(i).substring(oriPaths.get(i).lastIndexOf(".") + 1);//文件后缀名
                recoveryFile.setType(suffix);
            }
            recoveryFile.setOriginalPath(oriPaths.get(i));
            recoveryFile.setUsername(username);
            String filename = oriPaths.get(i).substring(oriPaths.get(i).lastIndexOf("/") + 1);
            recoveryFile.setPresentPath(dstPath + filename);
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
        recoveryFileService.MoveToRecovery(presentPath,originalPath);
        recoveryFileService.deleteRecoveryFile(id);
        return ResultUtil.success();
    }

    /**
     * 回收站多个文件还原
     * @param ids
     * @return
     * @throws IOException
     */
    @PostMapping("/user/recycle/restore/all")
    public Result restoreRecycleFile(@RequestBody List<Long> ids) throws IOException {
        for(int i = 0; i < ids.size(); i++) {
            RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(ids.get(i));
            String presentPath = recoveryFile.getPresentPath();
            String originalPath = recoveryFile.getOriginalPath();
            recoveryFileService.MoveToRecovery(presentPath, originalPath);
            recoveryFileService.deleteRecoveryFile(ids.get(i));
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
    public Result deleteRecycleFile(@RequestBody List<Long> ids) throws IOException {
        for(int i = 0; i < ids.size(); i++) {
            RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(ids.get(i));
            String presentPath = recoveryFile.getPresentPath();
            recoveryFileService.deleteFile(presentPath);
            recoveryFileService.deleteRecoveryFile(ids.get(i));
        }
        return ResultUtil.success();
    }
}
