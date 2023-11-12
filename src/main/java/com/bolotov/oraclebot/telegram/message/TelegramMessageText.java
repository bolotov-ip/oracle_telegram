package com.bolotov.oraclebot.telegram;

public interface TelegramTextMessage extends BasicMessage {

    public BasicMessage addButton(String name, String action);

    public BasicMessage addItemMenu(String name, String action);
}
