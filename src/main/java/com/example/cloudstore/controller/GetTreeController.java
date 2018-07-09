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
import java.util.List;
import java.util.Map;

@RestController
public class GetTreeController {

    @Autowired
    GetTreeService getTreeService;

    @PostMapping("/get/tree")
    public List<Map<String,Object>> GetTree(@RequestParam("path") String path) throws IOException, URISyntaxException {
        return getTreeService.FindTree(path);
    }
}
