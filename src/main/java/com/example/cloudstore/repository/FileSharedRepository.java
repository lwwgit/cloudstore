package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.FileShared;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileSharedRepository extends JpaRepository<FileShared, String>{
    Optional<FileShared> findById(String id);
}
