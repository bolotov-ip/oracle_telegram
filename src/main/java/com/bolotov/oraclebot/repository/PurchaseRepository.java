package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
