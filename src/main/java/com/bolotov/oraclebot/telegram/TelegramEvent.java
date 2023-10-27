package com.bolotov.oraclebot.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramEvent {

    public static TelegramEvent valueOf(Update update) {
        TelegramEvent event = new TelegramEvent();
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        event.setActionName(text);
        event.setChatId(chatId);
        return event;
    }

    private String actionName;

    private Long chatId;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
