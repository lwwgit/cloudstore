package com.example.cloudstore.service;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface GetTreeService {
    List<Map<String, Object>> FindTree(String path) throws URISyntaxException, IOException;

    void ChildDir(FileSystem hdfs, Path path, List<Map<String, Object>> ListMap) throws IOException;

    Boolean GrandDir(FileSystem hdfs, Path path) throws IOException;
}
