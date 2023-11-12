package com.bolotov.oraclebot.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name = "oracle_category")
public class OracleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private OracleCategory parentId;

    public String getHierarchy() {
        List<String> names = new ArrayList<>();
        OracleCategory oracleCategory = this;
        while(oracleCategory!=null) {
            names.add(oracleCategory.getName());
            oracleCategory = oracleCategory.getParentId();
        }
        Collections.reverse(names); /**Корневая категория сначала*/

        return String.join(";", names);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OracleCategory getParentId() {
        return parentId;
    }

    public void setParentId(OracleCategory parentId) {
        this.parentId = parentId;
    }
}