package com.bolotov.oraclebot.service.impl;

import com.bolotov.oraclebot.model.Balance;
import com.bolotov.oraclebot.model.Role;
import com.bolotov.oraclebot.model.SourceSet;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.repository.BalanceRepository;
import com.bolotov.oraclebot.repository.RoleRepository;
import com.bolotov.oraclebot.repository.UserRepository;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BalanceRepository balanceRepository;

    @Override
    public User getUser(Long chatId) {
        Optional<User> userOptional = userRepository.findById(chatId);
        if(userOptional.isPresent())
            return userOptional.get();
        return null;
    }

    @Override
    public User authentication(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User registration(TelegramEvent event) {
        User currentUser = new User();
        currentUser.setChatId(event.getChatId());
        currentUser.setUsername(event.getUsername());
        currentUser.setDateRegistration(new Timestamp(System.currentTimeMillis()));
        currentUser.setFirstname(event.getFirstname());
        currentUser.setLastname(event.getLastname());
        Set<Role> roleSet = new HashSet<>();
        Role userRole = getRole(Role.RoleName.USER);
        currentUser.setSelectRole(userRole);
        roleSet.add(userRole);
        if(currentUser.getUsername().equals("bolotov_ip")) {
            roleSet.add(getRole(Role.RoleName.ADMIN));
            roleSet.add(getRole(Role.RoleName.EXPERT));
            roleSet.add(getRole(Role.RoleName.OWNER));
        }
        currentUser.setRoles(roleSet);
        authentication(currentUser);
        return currentUser;
    }

    @Override
    public boolean addRole(User user, Role.RoleName roleName) {
        return false;
    }

    @Override
    public boolean deleteRole(User user, Role.RoleName roleName) {
        return false;
    }

    @Override
    public boolean deleteUser(User user) {
        return false;
    }

    @Override
    public List<User> getUserByRoleName(Role.RoleName roleName) {
        return null;
    }

    @Override
    public void selectSourceSet(User user, SourceSet set) {
        user.addSelectSource(set.getGroupName(), set.getName());
        userRepository.save(user);
    }

    @Override
    public Balance upBalance(User user, double value) {
        Balance balance = user.getBalance();
        if(balance == null) {
            balance = new Balance();
            balance.setUser(user);
            balance.setCurrency("\u20BD");
            user.setBalance(balance);
        }
        double currentValue = balance.getAmount();
        balance.setAmount(currentValue+value);
        userRepository.save(user);
        return balance;
    }

    private Role getRole(Role.RoleName roleName) {
        Role userRole = roleRepository.findByName(roleName);
        if(userRole == null) {
            userRole = new Role();
            userRole.setName(roleName);
            roleRepository.save(userRole);
        }

        return userRole;
    }
}
