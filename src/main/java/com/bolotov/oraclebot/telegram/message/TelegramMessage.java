package com.bolotov.oraclebot.telegram.message;

import com.bolotov.oraclebot.telegram.TelegramBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BasicMessage {

    public BasicMessage setChatId(Long chatId);

    public BasicMessage setBot(TelegramBot bot);

    public BasicMessage setText(String text);

    public Object send() throws TelegramApiException;
}
