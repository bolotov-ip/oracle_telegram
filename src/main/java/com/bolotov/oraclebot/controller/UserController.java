package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.annotation.TelegramAction;
import com.bolotov.oraclebot.annotation.TelegramController;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.TelegramMessage;
import org.springframework.beans.factory.annotation.Autowired;

@TelegramController
public class MenuController {

    @Autowired
    private UserService userService;

    @TelegramAction(action="/start")
    public void start(TelegramEvent event) {
        User currentUser = userService.getUser(event.getChatId());
        TelegramMessage telegramMessage = TelegramMessage.valueOf("Главное меню", event);
//        telegramMessage.setButtonLayout(2,1);
        telegramMessage.addButton("/free-product", "Тарологи");
        telegramMessage.addButton("/free-product", "Услуги");
        telegramMessage.addButton("/free-product", "Погадайте бесплатно");
        telegramMessage.send();
    }

    @TelegramAction(action="/free-product")
    public void freeRandomProduct(TelegramEvent event) {
        TelegramMessage telegramMessage = TelegramMessage.valueOf("Здесь будет показан случайный бесплатный продукт", event);
        telegramMessage.addButton("/start", "назад");
        telegramMessage.send();
    }
}
