package com.example.cloudstore.service;

import com.example.cloudstore.domain.entity.FileShared;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public interface FileSharedService {
    String CreateSharedLink(String path) throws URISyntaxException, IOException;

    Optional<FileShared> ToShared(String id);
}
