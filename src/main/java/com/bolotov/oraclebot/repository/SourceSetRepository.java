package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.SourceSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceSetRepository extends JpaRepository<SourceSet, Long> {

    public SourceSet findByName(String name);
}
