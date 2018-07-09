package com.example.cloudstore.controller;

import com.example.cloudstore.domain.Result;
import com.example.cloudstore.service.GetTreeService;
import com.example.cloudstore.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class GetTreeController {

    @Autowired
    GetTreeService getTreeService;

    @PostMapping("/get/tree")
    public Result GetTree(@RequestParam("path") String path) throws IOException, URISyntaxException {
        return ResultUtil.success(getTreeService.FindTree(path));
    }
}
