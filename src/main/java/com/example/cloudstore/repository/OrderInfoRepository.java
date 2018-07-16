package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Integer>{
}
