package com.bolotov.oraclebot.telegram.message.impl;

import com.bolotov.oraclebot.telegram.TelegramBot;
import com.bolotov.oraclebot.telegram.message.TelegramMessage;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMenu;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramMessageMenuImpl extends TelegramMessageTextImpl implements TelegramMessageMenu {

    List<BotCommand> botCommands = new ArrayList<>();

    @Override
    public void send() throws TelegramApiException {
        BotCommandScope scope = new BotCommandScopeChat(String.valueOf(chatId));
        SetMyCommands commands = new SetMyCommands(botCommands, scope, null);
        bot.execute(commands);
    }

    @Override
    public TelegramMessage addItemMenu(String name, String action) {
        botCommands.add(new BotCommand(action, name));
        return this;
    }
}
