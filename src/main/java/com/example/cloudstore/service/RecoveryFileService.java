package com.example.cloudstore.service;


import com.example.cloudstore.domain.entity.RecoveryFile;

import java.io.IOException;
import java.util.List;

public interface RecoveryFileService {
    /**
     * 文件移动
     * @param oriPath 原路径
     * @param dstPath 将要移动的路径
     * @return
     * @throws IOException
     */
    boolean MoveToRecovery(String oriPath, String dstPath) throws IOException;

    /**
     * 回收站文件删除
     * @param filePath
     * @return
     * @throws IOException
     */
    boolean deleteFile(String filePath) throws IOException;

    /**
     * 文件移入回收站时插入数据库
     * @param recoveryFile
     * @return
     */
    RecoveryFile insert(RecoveryFile recoveryFile);

    /**
     * 删除回收站文件
     * @param id
     */
    void deleteRecoveryFile(Long id);

    List<RecoveryFile> findByUsername(String username);

    RecoveryFile findByRecoveryId(Long recoveryId);

    RecoveryFile findByPresentPath(String presentPath);

}
