package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Message;
import com.bolotov.oraclebot.service.impl.MessageServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class MessageRepositoryImpl implements MessageRepository {

    @Override
    @Cacheable("messages")
    public Message getMessage(Long id) {
        Message msg =  new Message();
        msg.setOwnerId(id);
        return msg;
    }

    @Override
    @CacheEvict("messages")
    public void removeMessage(Long userId) {
        System.out.println(String.format("Удалено сообщение дял %d", userId));
    }
}