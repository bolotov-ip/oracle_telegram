package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Oracle;
import com.bolotov.oraclebot.model.OracleCategory;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OracleRepository extends JpaRepository<Oracle, Long> {
    public List<Oracle> findByCategory(OracleCategory category);
}
