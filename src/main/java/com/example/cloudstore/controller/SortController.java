package com.example.cloudstore.controller;
import com.example.cloudstore.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
public class SortController {

    @Autowired
    private SortService sortService;

    @PostMapping("/sort")
    public List<Map<String, Object>> SortFile( int flag) throws IOException, URISyntaxException {

//        System.out.println("######" + flag);
        List<Map<String,Object>> list = sortService.SortFile(flag);
//        System.out.println("##########" + list);
        return list;

    }
}
