package com.example.cloudstore.repository;


import com.example.cloudstore.domain.entity.RecoveryFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecoveryFileRepository extends JpaRepository<RecoveryFile,Long> {

    List<RecoveryFile> findByUsername(String username);

    RecoveryFile findByRecoveryId(Long recoveryId);

    RecoveryFile findByPresentPath(String presentPath);
}
