package com.example.cloudstore.controller;

import com.example.cloudstore.domain.Result;
import com.example.cloudstore.service.FileSharedService;
import com.example.cloudstore.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class FileSharedController {

    @Autowired
    FileSharedService fileSharedService;

    //获取分享链接
    @PostMapping("/get/shared/link")
    public Result GetSharedLink(@RequestParam("filePath") String[] paths,
                                @RequestParam("ifPasswd") String ifPasswd) throws IOException, URISyntaxException {
        System.out.println("打印filePath" + paths + "\n是否密码" + ifPasswd);
        return ResultUtil.success(fileSharedService.CreateSharedLink(paths, ifPasswd));
    }

    //验证该链接是否为加密链接，返回yes/no给前端
    @PostMapping("/home/share")
    public Result ShareVerify(@RequestParam("id") String id,
                              @RequestParam("username") String username) {
        String ifPasswd = fileSharedService.ShareVerify(id, username);
        if (ifPasswd.equals("yes")){
            return ResultUtil.success(ifPasswd);
        }
        if (ifPasswd.equals("no")){
            return ResultUtil.success(fileSharedService.ToShare(id, "-1"));
        }
        return ResultUtil.error(0, "ifPasswd is not yes/no");
    }

    //显示当前用户所有分享
    @PostMapping("/home/share/all")
    public Result AllShare() {
        return ResultUtil.success(fileSharedService.AllShare());
    }

    //根据链接id和passwd，返回链接信息给前端
    @PostMapping("/home/share/goto")
    public Result ToShare(@RequestParam("id") String id,
                          @RequestParam("passwd") String passwd) {
        return ResultUtil.success(fileSharedService.ToShare(id, passwd));
    }

    //根据分享文件的路径和链接id，取消分享链接（在数据库中删除）
    @PostMapping("/home/share/remove")
    public Result removeShare(@RequestParam("id") String[] id) {
        return ResultUtil.success(fileSharedService.RemoveShare(id));
    }

    @PostMapping("/home/share/report")
    public Result report(@RequestParam("id") String id){
        return ResultUtil.success(fileSharedService.Report(id));
    }
}
