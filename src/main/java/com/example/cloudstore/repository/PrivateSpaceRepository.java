package com.example.cloudstore.repository;

import com.example.cloudstore.domain.PrivateSpace;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author jitdc
 * @Date Create in 15:22 2018/7/13
 * @Description:
 */
public interface PrivateSpaceRepository extends JpaRepository<PrivateSpace,Integer> {
    PrivateSpace findByUsername(String username);
}
