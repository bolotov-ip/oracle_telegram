package com.bolotov.oraclebot.telegram.message;

import com.bolotov.oraclebot.telegram.TelegramBot;
import com.bolotov.oraclebot.telegram.message.TelegramMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramMessageMenu extends TelegramMessage {
    public TelegramMessage addItemMenu(String name, String action);
}
