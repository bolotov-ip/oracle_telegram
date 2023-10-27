package com.bolotov.oraclebot.repository;


import com.bolotov.oraclebot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
