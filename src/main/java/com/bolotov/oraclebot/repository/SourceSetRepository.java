package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.OracleCategory;
import com.bolotov.oraclebot.model.SourceSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SourceSetRepository extends JpaRepository<SourceSet, Long> {

    public SourceSet findByName(String name);

    @Query("SELECT ss.groupName FROM source_set ss group by ss.groupName")
    public List<String> getAllGroups();

    public List<SourceSet> findByGroupName(String groupName);
}
