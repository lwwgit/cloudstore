package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.FileShared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface FileSharedRepository extends JpaRepository<FileShared, String> {

    List<FileShared> findAllByCharId(String charId);

    FileShared findByCharId(String charId);

    @Modifying
    @Transactional
    @Query("delete from FileShared file where file.charId=?1")
    void deleteAllByCharId(String charId);

}
