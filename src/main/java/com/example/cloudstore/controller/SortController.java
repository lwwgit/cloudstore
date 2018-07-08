package com.example.cloudstore.controller;
import com.example.cloudstore.service.SearchService;
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

    @Autowired
    private SearchService searchService;

    @PostMapping("/sort")
//    @RequestMapping(value = "/sort", method = RequestMethod.GET)
    public List<Map<String, Object>> SortFile(Integer flag) throws IOException, URISyntaxException {

        List<Map<String,Object>> list = sortService.SortFile(flag);
        return list;

    }

    @PostMapping("/search/file")
    public List<Map<String, Object>> SearchFile(@RequestParam("SearchWord") String SearchWord) throws IOException, URISyntaxException {
        return searchService.SearchFile(SearchWord);
    }
}
