package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.service.impl.UserServiceImpl;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@TelegramController
public class InformationController {

    @Autowired
    TelegramMessageFactory messageFactory;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @TelegramAction(action="/error")
    public void error(TelegramEvent event) {

        TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, event.getText());
        telegramMessage.addButton( "На главную", "/start");
        try {
            telegramMessage.send();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
