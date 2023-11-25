package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {



}
