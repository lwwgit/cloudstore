package com.example.cloudstore.service;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface SortService {
    List<Map<String, Object>> SortFile(int flag) throws URISyntaxException, IOException;

    void ShowFile(FileSystem hdfs, Path path, List<Map<String, Object>> ListMap, int flag) throws IOException;

}
