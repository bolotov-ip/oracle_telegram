package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.TelegramMessage;
import org.springframework.beans.factory.annotation.Autowired;

@TelegramController
public class UserController {

    @Autowired
    private UserService userService;



    @TelegramAction(action="/user-free-product")
    public void freeRandomProduct(TelegramEvent event) {
        TelegramMessage telegramMessage = TelegramMessage.valueOf("Здесь будет показан случайный бесплатный продукт", event);
        telegramMessage.addButton("/start", "назад");
        telegramMessage.send();
    }
}
