package com.example.cloudstore.service;

import com.example.cloudstore.domain.entity.FileShared;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface FileSharedService {
    String CreateSharedLink(String[] paths) throws URISyntaxException, IOException;

    List<FileShared> ToShared(String id);
}
