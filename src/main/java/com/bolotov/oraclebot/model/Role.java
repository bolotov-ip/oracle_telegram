package com.bolotov.oraclebot.model;

import com.bolotov.oraclebot.repository.RoleRepository;
import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "roles")
public class Role {

    public enum RoleName {
        USER, ADMIN, OWNER;
    }

    @Id
    private Long id;
    @Enumerated
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
