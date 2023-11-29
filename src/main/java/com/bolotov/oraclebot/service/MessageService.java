package com.bolotov.oraclebot.service;

import com.bolotov.oraclebot.model.User;

public interface MessageService {

    /**порядок args: telegramId, caption, groupId*/
    public void addPhoto(User user, String... args);
    /**порядок args: telegramId, caption, groupId*/
    public void addVideo(User user, String... args);

    public void addDocument(User user, String documentId);

    public void addVoice(User user, String voiceId);

    public void addText(User user, String text);

    public void removeMessage(Long userId);

    public void sendCurrentMessage(User user, User recipient);
}
