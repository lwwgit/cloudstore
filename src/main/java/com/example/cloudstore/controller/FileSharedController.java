package com.example.cloudstore.controller;

import com.example.cloudstore.domain.Result;
import com.example.cloudstore.domain.entity.FileShared;
import com.example.cloudstore.service.FileSharedService;
import com.example.cloudstore.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class FileSharedController {

    @Autowired
    FileSharedService fileSharedService;

    @PostMapping("/get/shared/link")
    public Result GetSharedLink(@RequestParam("filePath") String path) throws IOException, URISyntaxException {

        return ResultUtil.success(fileSharedService.CreateSharedLink(path));
    }

    @PostMapping("/home/share")
    public Result ToShared(@RequestParam("id") String id) {
        return ResultUtil.success(fileSharedService.ToShared(id));
    }
}
