package com.example.cloudstore.service;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface SearchService {
    List<Map<String, Object>> SearchFile(String SearchWord) throws URISyntaxException, IOException;

    void ReturnSearch(FileSystem hdfs, Path path, List<Map<String, Object>> ListMap, Pattern pattern) throws IOException;
}
