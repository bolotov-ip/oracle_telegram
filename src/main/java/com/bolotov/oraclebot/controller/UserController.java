package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.model.Balance;
import com.bolotov.oraclebot.model.User;
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

    @TelegramAction(action="/upbalance")
    public void upBalance(TelegramEvent event) throws TelegramApiException {
        User user = userServiceImpl.getUser(event.getChatId());
        double value = 3000;
        Balance balance = userServiceImpl.upBalance(user, 3000);
        TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, String.format("Ваш текущий баланс %.2f %s", balance.getAmount(), balance.getCurrency()));
        telegramMessage.addButton( "назад", "/start");
        telegramMessage.send();
    }
}
