package com.example.cloudstore.service;

import java.io.IOException;

public interface CopyFileService {

      boolean checkIsExist(String filePath) throws IOException;

      boolean copyFile(String srcPath, String dstPath) throws IOException;

      boolean copyDir(String src, String dsc) throws  IOException;
}
