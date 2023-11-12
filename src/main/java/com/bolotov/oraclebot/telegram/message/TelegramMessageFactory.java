package com.bolotov.oraclebot.telegram.message;

import com.bolotov.oraclebot.telegram.TelegramEvent;

public interface TelegramMessageFactory {

    public TelegramMessage newTelegramMessagePhoto(Long chatId, String fileTelegramId, String text);

    public TelegramMessage newTelegramMessageVideo(Long chatId, String fileTelegramId, String text);

    public TelegramMessageMedia newTelegramMessageMedia(Long chatId, String text) ;

    public TelegramMessageText newTelegramMessageText(TelegramEvent event, String text);

    public TelegramMessageText newTelegramMessageEditText(TelegramEvent event, String text);

    public TelegramMessageMenu newTelegramMessageMenu(Long chatId);
}
