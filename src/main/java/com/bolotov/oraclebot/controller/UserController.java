package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.service.impl.UserServiceImpl;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@TelegramController
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    TelegramMessageFactory messageFactory;

    @TelegramAction(action="/user/freeproduct")
    public void freeRandomProduct(TelegramEvent event) throws TelegramApiException {
        TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Здесь будет показан случайный бесплатный продукт");
        telegramMessage.addButton( "назад", "/start");
        telegramMessage.send();
    }
}
