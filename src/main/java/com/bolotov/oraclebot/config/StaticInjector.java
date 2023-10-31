package com.bolotov.oraclebot.config;

import com.bolotov.oraclebot.model.Role;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.repository.RoleRepository;
import com.bolotov.oraclebot.repository.UserRepository;
import com.bolotov.oraclebot.telegram.TelegramMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaticInjector {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TelegramBot bot;

    @PostConstruct
    public void injection() throws Exception
    {
        setUserRepository(userRepository);
        setRoleRepository(roleRepository);
        setBot(bot);
    }

    public void setUserRepository(UserRepository userRepository) {
        User.setUserRepository(userRepository);
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        Role.setRoleRepository(roleRepository);
    }

    public void setBot(TelegramBot bot) {
        TelegramMessage.setBot(bot);
    }


}
