package com.bolotov.oraclebot.service;

import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.repository.RoleRepository;
import com.bolotov.oraclebot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public User getUser(Long chatId) {
        Optional<User> userOptional = userRepository.findById(chatId);
        if(userOptional.isPresent())
            return userOptional.get();
        return null;
    }
}
