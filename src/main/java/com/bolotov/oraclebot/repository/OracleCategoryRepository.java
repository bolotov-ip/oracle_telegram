package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.OracleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OracleCategoryRepository extends JpaRepository<OracleCategory, Long> {

    @Query("SELECT oc FROM oracle_category oc where oc.name =:nameCategory and oc.parentId =:parentCategory")
    public OracleCategory getCategory(@Param("nameCategory") String nameCategory,  @Param("parentCategory") OracleCategory parentCategory);


    @Query("SELECT oc FROM oracle_category oc where oc.name =:nameCategory and oc.parentId is null")
    public OracleCategory getCategoryWithParentNull(@Param("nameCategory") String nameCategory);


    @Query("SELECT oc FROM oracle_category oc where oc.parentId =:parentCategory")
    public List<OracleCategory> getCategoriesByParent( @Param("parentCategory") OracleCategory parentCategory);

    @Query("SELECT oc FROM oracle_category oc where oc.parentId is null")
    public List<OracleCategory> getCategoryRoot();
}
