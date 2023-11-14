package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.exception.AddOracleException;
import com.bolotov.oraclebot.model.Source;
import com.bolotov.oraclebot.model.SourceSet;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.service.OracleDataService;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.service.impl.UserServiceImpl;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.telegram.message.TelegramMessage;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@TelegramController
public class SourceController {

    @Autowired
    OracleDataService oracleDataService;

    @Autowired
    TelegramMessageFactory messageFactory;

    @Autowired
    private UserService userService;

    @TelegramAction(action="/add_source")
    public String addSource(TelegramEvent event) {
        try {
            oracleDataService.addSourceSet(event.getText());
            TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Загрузка ресурсов успешно выполнена");
            telegramMessage.addButton( "Назад", "/start");
            telegramMessage.send();
        } catch (Exception e) {
            event.setText("Не удалось добавить ресурс.\nТекст ошибки:\n" + e.getMessage());
            return "/error";
        }
        return null;
    }

    @TelegramAction(action="/src_groups")
    public String viewGroups(TelegramEvent event) {
        try {
            TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Выберите группу медиа");
            List<String> groups = oracleDataService.getAllGroupsSource();
            for(String groupName : groups) {
                telegramMessage.addButton(groupName, "/src_sets?groupName=" +  groupName);
            }
            telegramMessage.addButton( "Назад", "/start");
            telegramMessage.send();
        } catch (Exception e) {
            event.setText("Ошибка\n" + e.getMessage());
            return "/error";
        }
        return null;
    }

    @TelegramAction(action="/src_sets")
    public String viewSets(TelegramEvent event) {
        try {
            String groupName = event.getValues().get("groupName");
            TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Выберите медиа ресурс");
            List<SourceSet> sourceSets = oracleDataService.getSourceSetByGroup(groupName);
            User currentUser = userService.getUser(event.getChatId());
            for(SourceSet set : sourceSets) {
                if(currentUser.getSelectSources().get(groupName).equals(set.getName()))
                    telegramMessage.addButton(set.getName() + " v", String.format("/sel_set?setId=%s", set.getId()));
                else
                    telegramMessage.addButton(set.getName(), String.format("/sel_set?setId=%s", set.getId()));
            }
            telegramMessage.addButton( "Назад", "/src_groups");
            telegramMessage.send();
        } catch (Exception e) {
            event.setText("Ошибка\n" + e.getMessage());
            return "/error";
        }
        return null;
    }

    @TelegramAction(action="/sel_set")
    public String selectSets(TelegramEvent event) {
        try {
            Long setId = Long.valueOf(event.getValues().get("setId"));
            SourceSet sourceSet = oracleDataService.getSourceSet(setId);
            User currentUser = userService.getUser(event.getChatId());
            userService.selectSourceSet(currentUser, sourceSet);

            TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Успех");
            telegramMessage.addButton( "В главное меню", "/start");
            telegramMessage.send();
        } catch (Exception e) {
            event.setText("Ошибка\n" + e.getMessage());
            return "/error";
        }
        return null;
    }
}
