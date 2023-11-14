package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Source;
import com.bolotov.oraclebot.model.SourceSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<Source, Long> {
}
