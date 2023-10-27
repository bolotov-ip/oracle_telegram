package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
