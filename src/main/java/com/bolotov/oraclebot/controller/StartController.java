package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.TelegramMessage;
import org.springframework.beans.factory.annotation.Autowired;

@TelegramController
public class StartController {

    @Autowired
    private UserService userService;

    @TelegramAction(action="/start")
    public void start(TelegramEvent event) {
        User currentUser = userService.getUser(event.getChatId());
        TelegramMessage telegramMessage = TelegramMessage.valueOf("Главное меню", event);
        telegramMessage.setButtonLayout(2,1);
        telegramMessage.addButton("/user-free-product", "Тарологи");
        telegramMessage.addButton("/user-free-product", "Услуги");
        telegramMessage.addButton("/user-free-product", "Погадайте бесплатно");
        telegramMessage.send();
    }
}
