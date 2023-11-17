package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Oracle;
import com.bolotov.oraclebot.model.OracleCategory;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OracleRepository extends JpaRepository<Oracle, Long> {

    @Query("SELECT oc FROM oracle oc where oc.category =:category")
    public List<Oracle> getOraclesByCategory( @Param("category") OracleCategory category);
}
