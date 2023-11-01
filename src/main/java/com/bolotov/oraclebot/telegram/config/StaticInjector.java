package com.bolotov.oraclebot.telegram.config;

import com.bolotov.oraclebot.model.Role;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.repository.RoleRepository;
import com.bolotov.oraclebot.repository.UserRepository;
import com.bolotov.oraclebot.telegram.TelegramBot;
import com.bolotov.oraclebot.telegram.TelegramMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaticInjector {

    @Autowired
    private TelegramBot bot;

    @PostConstruct
    public void injection() throws Exception
    {
        setBot(bot);
    }

    public void setBot(TelegramBot bot) {
        TelegramMessage.setBot(bot);
    }


}
