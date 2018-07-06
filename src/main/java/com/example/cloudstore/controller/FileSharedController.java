package com.example.cloudstore.controller;

import com.example.cloudstore.domain.entity.FileShared;
import com.example.cloudstore.service.FileSharedService;
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

    @PostMapping("/GetSharedLink")
    public String GetSharedLink(@RequestParam("FilePath") String path) throws IOException, URISyntaxException {

        String result = fileSharedService.CreateSharedLink(path);

        return result;
    }

    @PostMapping("/share.html")
    public Optional<FileShared> ToShared(@RequestParam("id") String id) {

        return fileSharedService.ToShared(id);
    }
}
