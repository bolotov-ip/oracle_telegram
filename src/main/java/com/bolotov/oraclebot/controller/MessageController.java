package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.model.Purchase;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.service.MessageService;
import com.bolotov.oraclebot.service.OracleService;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@TelegramController
public class MessageController {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    OracleService oracleService;

    @Autowired
    TelegramMessageFactory messageFactory;

    @TelegramAction(action="/text")
    public String text(TelegramEvent event) {
        User user = userService.getUser(event.getChatId());
        messageService.addText(user, event.getText());
        return "/confirm";
    }

    @TelegramAction(action="/media")
    public String media(TelegramEvent event) {
        User user = userService.getUser(event.getChatId());
        if(event.isPhoto()) {
            if(event.isGroup())
                messageService.addPhoto(user, event.getPhotoId(), event.getText(), event.getGroupId());
            else
                messageService.addPhoto(user, event.getPhotoId(), event.getText(), event.getGroupId());
        }
        else if(event.isVideo()) {
            if(event.isGroup())
                messageService.addVideo(user, event.getPhotoId(), event.getText(), event.getGroupId());
            else
                messageService.addVideo(user, event.getPhotoId(), event.getText(), event.getGroupId());
        }
        return "/confirm";
    }

    @TelegramAction(action="/confirm")
    public String confirm(TelegramEvent event) {
        try {
            TelegramMessageText messageText = messageFactory.newTelegramMessageText(event, "Вы хотите отправить ответ?\nЕсли ответ не готов то отправьте текст, фото, видео, документ или голосовое");
            messageText.addButton("Отправить", "/sendMessage");
            messageText.send();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @TelegramAction(action="/sendMessage")
    public String sendMessage(TelegramEvent event) {
        try {
            User user = userService.getUser(event.getChatId());
            Purchase purchase = oracleService.getSelectedPurchase(user);
            User customer = purchase.getCustomer();
            messageService.sendCurrentMessage(user, customer);
            TelegramMessageText messageText = messageFactory.newTelegramAdaptiveMessageText(event, "Вы хотите отправить ответ?\nЕсли ответ не готов то отправьте текст, фото, видео, документ или голосовое");
            messageText.addButton("К заказам", "/all_purchase");
            messageText.send();
            oracleService.donePurchase(purchase);
        }
        catch (Exception e) {

        }
        return  null;

    }
}
