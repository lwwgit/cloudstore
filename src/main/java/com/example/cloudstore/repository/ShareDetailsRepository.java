package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.ShareDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ShareDetailsRepository extends JpaRepository<ShareDetails, Integer> {

    ShareDetails findByCharId(String charId);

    List<ShareDetails> findAll();

    @Modifying
    @Transactional
    @Query("delete from ShareDetails sd where sd.charId=?1")
    void deleteByCharId(String charId);


}
