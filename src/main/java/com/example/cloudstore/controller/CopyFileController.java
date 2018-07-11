package com.example.cloudstore.controller;


import com.example.cloudstore.domain.Result;
import com.example.cloudstore.service.CopyFileService;
import com.example.cloudstore.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CopyFileController {
    @Autowired
    private CopyFileService copyFileService;

    /**
     * 单个文件复制
     * @param srcPath
     * @param dstPath
     * @return
     * @throws IOException
     */
    @PostMapping("/user/file/copy")
    public Result copyFile(String srcPath, String dstPath) throws Exception {
        boolean data = copyFileService.copyDir(srcPath,dstPath);
        if (data == true) {
            return ResultUtil.success();
        }
        return ResultUtil.error(1,"文件（夹）已存在");
    }

    /**
     * 多个文件复制
     * @param srcPaths
     * @param dstPath
     * @return
     * @throws IOException
     */
    @PostMapping("/user/file/copy/all")
    public Result copyAllFile(String[] srcPaths, String dstPath) throws IOException {
        for (String srcPath:srcPaths) {
            String filename = srcPath.substring(srcPath.lastIndexOf("/") + 1);
            String checkName = dstPath + "/" + filename;
            if(srcPath.equals(dstPath)){
                return ResultUtil.error(2,"复制失败，不能将文件复制到自身或其子目录下");
            }
            if (dstPath.startsWith(srcPath.substring(0,srcPath.length()))){
                return ResultUtil.error(2,"复制失败，不能将文件复制到自身或其子目录下");
            }
            if(copyFileService.checkIsExist(checkName) == true) {
                return ResultUtil.error(1,"复制失败，文件（夹）已存在");
            }
        }

        for (String srcPath:srcPaths){
            boolean data = copyFileService.copyDir(srcPath,dstPath);
        }
        return  ResultUtil.success();
    }
}
