package com.bolotov.oraclebot.telegram.message;

import com.bolotov.oraclebot.telegram.TelegramBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface TelegramMessage {

    public TelegramMessage setChatId(Long chatId);

    public TelegramMessage setBot(TelegramBot bot);

    public TelegramMessage setText(String text);

    public void send() throws TelegramApiException;
}
