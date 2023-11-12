package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.exception.AddOracleException;
import com.bolotov.oraclebot.service.OracleDataService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.telegram.message.TelegramMessage;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;

@TelegramController
public class SourceController {

    @Autowired
    OracleDataService oracleDataService;

    @Autowired
    TelegramMessageFactory messageFactory;

    @TelegramAction(action="/source")
    public void sourceMenu(TelegramEvent event) {

        TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Загрузка ресурсов");
        telegramMessage.addButton("Добавить ресурс", "/source/scanning");
        telegramMessage.addButton( "Назад", "/start");

        try {
            telegramMessage.send();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @TelegramAction(action="/source_add")
    public void addSource(TelegramEvent event) {
        TelegramMessageText telegramMessage = null;
        try {
            oracleDataService.addOracle(event.getText());
            telegramMessage = messageFactory.newTelegramMessageText(event, "Загрузка ресурсов");
            telegramMessage.addButton("Добавить ресурс", "/source/scanning");

        } catch (AddOracleException e) {
            telegramMessage = messageFactory.newTelegramMessageText(event, e.getMessage());
        }
        telegramMessage.addButton( "Назад", "/start");
        try {
            telegramMessage.send();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @TelegramAction(action="/source/scanning")
    public void sourceScan(TelegramEvent event) {
//        try {
//            oracleDataService.synchronizeWithFiles();
//            TelegramMessage telegramMessage = TelegramMessage.valueOf("Успешно", event);
//            telegramMessage.addButton("/start", "Назад");
//            telegramMessage.send();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

    }
}
