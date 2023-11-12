package com.bolotov.oraclebot.telegram.message.impl;

import com.bolotov.oraclebot.telegram.TelegramBot;
import com.bolotov.oraclebot.telegram.message.TelegramMessage;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class TelegramMessageImpl implements TelegramMessage {

    protected TelegramBot bot;

    protected String text;

    protected Long chatId;

    @Override
    public TelegramMessage setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    @Override
    public TelegramMessage setBot(TelegramBot bot) {
        this.bot = bot;
        return this;
    }

    @Override
    public TelegramMessage setText(String text) {
        this.text = text;
        return null;
    }

    @Override
    public void send() throws TelegramApiException{

    }
}
