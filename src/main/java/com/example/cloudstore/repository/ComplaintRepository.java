package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ComplaintRepository extends JpaRepository<Complaint,Integer> {

   Complaint findByUsername(String username);

    void deleteComplaintByUsername(String username);
}
