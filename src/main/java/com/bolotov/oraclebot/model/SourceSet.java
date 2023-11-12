package com.bolotov.oraclebot.model;

import jakarta.persistence.*;

import java.util.*;

@Entity(name = "source_set")
public class MediaSourceSet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "name")
    private String groupName;

    @ElementCollection
    @CollectionTable(name="source", joinColumns = @JoinColumn(name = "source_set_id"))
    @MapKeyColumn(name = "key")
    @Column(name = "telegram_id")
    private Map<String, String> sources = new HashMap<>();

    public void addSource(String key, String telegramId) {
        sources.put(key, telegramId);
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Map<String, String> getSources() {
        return sources;
    }

    public void setSources(Map<String, String> sources) {
        this.sources = sources;
    }
}
