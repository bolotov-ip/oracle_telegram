package com.bolotov.oraclebot.service;

import com.bolotov.oraclebot.model.Balance;
import com.bolotov.oraclebot.model.Role;
import com.bolotov.oraclebot.model.SourceSet;
import com.bolotov.oraclebot.model.User;

import java.util.List;

public interface UserService {

    public User getUser(Long chatId);

    public User authentication(User user);

    public boolean addRole(User user, Role.RoleName roleName);

    public boolean deleteRole(User user, Role.RoleName roleName);

    public boolean deleteUser(User user);

    public List<User> getUserByRoleName(Role.RoleName roleName);

    public void selectSourceSet(User user, SourceSet set);

    public Balance upBalance(User user, double value);
}
