package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMenu;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@TelegramController
public class StartController {

    @Autowired
    private UserService userService;

    @Autowired
    TelegramMessageFactory messageFactory;

    @TelegramAction(action="/start")
    public void start(TelegramEvent event) {
        TelegramMessageMenu messageMenu = messageFactory.newTelegramMessageMenu(event.getChatId());
        messageMenu.addItemMenu("Главное меню", "/start");
        messageMenu.addItemMenu("Настроить отображение медиа", "/src_groups");
        messageMenu.addItemMenu("Пополнить баланс", "/upbalance");
        messageMenu.addItemMenu("Свободные заказы", "/all_purchase");
        messageMenu.addItemMenu("Свободные заказы", "/all_purchase");

        User currentUser = userService.getUser(event.getChatId());
        if(currentUser == null) {
            currentUser = new User();
            currentUser.setChatId(event.getChatId());
            currentUser.setUsername(event.getUsername());
            userService.authentication(currentUser);
        }

        TelegramMessageText telegramMessage = messageFactory.newTelegramAdaptiveMessageText(event, "Главное меню");
        telegramMessage.addLayoutButton(2,1);
        telegramMessage.addButton("Тарологи", "/user/freeproduct");
        telegramMessage.addButton("Услуги", "/view_group");
        telegramMessage.addButton( "Погадайте бесплатно", "/user/freeproduct");
        try {
            messageMenu.send();
            telegramMessage.send();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
