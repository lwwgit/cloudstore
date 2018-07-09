package com.example.cloudstore.domain.entity;

import java.util.List;

public class JsonDelete {
    private List<String> filePath;

    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }
}
