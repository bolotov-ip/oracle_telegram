package com.bolotov.oraclebot.model;

import jakarta.persistence.*;

import java.util.*;

@Entity(name = "source_set")
public class SourceSet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "group_name")
    private String groupName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "source_set_id")
    Set<Source> sources = new HashSet<>();

    public void addSource(String key, String telegramId, Source.Type type) {
        Source source = new Source();
        source.setName(key);
        source.setTelegramId(telegramId);
        source.setType(type);
        sources.add(source);
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

    public Set<Source> getSources() {
        return sources;
    }

    public void setSources(Set<Source> sources) {
        this.sources = sources;
    }
}
