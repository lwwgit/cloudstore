package com.example.cloudstore.service;

import com.example.cloudstore.domain.JsonResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.List;

public interface MainService {
    void decompress(String path) throws URISyntaxException;
    JsonResult mkdirMulu(String pPath, String fileName) throws URISyntaxException;
    JsonResult lookdir(String muluName) throws URISyntaxException;
    JsonResult rename( String oldPath, String newName) throws URISyntaxException;
    List<JsonResult> deleteHDFS(String[] fileDelPaths) throws URISyntaxException;
    List<JsonResult> move(String[] oldDirPaths , String newFatherPath) throws URISyntaxException;
    ResponseEntity<JsonResult> upload(MultipartFile file, String mulupath) throws URISyntaxException;
}
