package com.bolotov.oraclebot.model;

import com.bolotov.oraclebot.repository.RoleRepository;
import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "roles")
public class Role {

    public enum RoleName {
        USER, EXPERT, ADMIN, OWNER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName name;
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

}
