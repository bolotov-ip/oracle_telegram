package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.annotation.TelegramAction;
import com.bolotov.oraclebot.annotation.TelegramController;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import org.springframework.beans.factory.annotation.Autowired;

@TelegramController
public class MenuController {

    @Autowired
    private UserService userService;

    @TelegramAction(action="/start")
    public void start(TelegramEvent event) {
        User currentUser = userService.getUser(event.getChatId());
        if(currentUser.isAdmin()) {

        }
        else {
            
        }
    }
}
