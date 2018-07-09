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

    @PostMapping("/get/shared/link")
    public Result GetSharedLink(@RequestParam("filePath") String[] paths) throws IOException, URISyntaxException {

        System.out.println("批量分享: " + paths);

        return ResultUtil.success(fileSharedService.CreateSharedLink(paths));
    }

    @PostMapping("/home/share")
    public Result ToShared(@RequestParam("id") String id) {
        return ResultUtil.success(fileSharedService.ToShared(id));
    }
}
