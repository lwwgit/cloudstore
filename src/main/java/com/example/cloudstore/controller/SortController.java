package com.example.cloudstore.controller;
import com.example.cloudstore.domain.Result;
import com.example.cloudstore.service.SearchService;
import com.example.cloudstore.service.SortService;
import com.example.cloudstore.utils.ResultUtil;
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

    @Autowired
    private SearchService searchService;

    @PostMapping("/sort")
    public Result SortFile(Integer flag) throws IOException, URISyntaxException {

        List<Map<String,Object>> list = sortService.SortFile(flag);
        return ResultUtil.success(list);

    }

    @PostMapping("/sort/capacity")
    public Result SortCapacity() throws IOException, URISyntaxException {
        return ResultUtil.success(sortService.SortCapacity());
    }

    @PostMapping("/search/file")
    public Result SearchFile(@RequestParam("searchWord") String SearchWord) throws IOException, URISyntaxException {
        return ResultUtil.success(searchService.SearchFile(SearchWord));
    }


}
