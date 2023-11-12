package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.service.impl.UserServiceImpl;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMenu;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@TelegramController
public class StartController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    TelegramMessageFactory messageFactory;

    @TelegramAction(action="/start")
    public void start(TelegramEvent event) {
        TelegramMessageMenu messageMenu = messageFactory.newTelegramMessageMenu(event.getChatId());
        messageMenu.addItemMenu("Ресурсы", "/source");

        User currentUser = userServiceImpl.getUser(event.getChatId());
        TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Главное меню");
        telegramMessage.addLayoutButton(2,1);
        telegramMessage.addButton("Тарологи", "/user/freeproduct");
        telegramMessage.addButton("Услуги", "/user/freeproduct");
        telegramMessage.addButton( "Погадайте бесплатно", "/user/freeproduct");
        try {
            messageMenu.send();
            telegramMessage.send();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
