package com.bolotov.oraclebot.model;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity(name = "oracle")
public class Oracle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name="oracle_result", joinColumns = @JoinColumn(name = "oracle_id"))
    @MapKeyColumn(name = "key")
    @Column(name = "message")
    private Map<String, String> oracleResult = new HashMap<>();

    private double price;

    private String description;

    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_chat_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private OracleCategory category;

    public OracleCategory getCategory() {
        return category;
    }

    public void setCategory(OracleCategory category) {
        this.category = category;
    }

    public void addOracleResult(String key, String value) {
        oracleResult.put(key, value);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getOracleResult() {
        return oracleResult;
    }

    public void setOracleResult(Map<String, String> oracleResult) {
        this.oracleResult = oracleResult;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}