package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Message;
import com.bolotov.oraclebot.service.impl.MessageServiceImpl;

public interface MessageRepository {
    Message getMessage(Long id);

    public void removeMessage(Long userId);
}
