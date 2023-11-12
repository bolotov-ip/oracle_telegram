package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Oracle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleRepository extends JpaRepository<Oracle, Long> {
}
