package com.bolotov.oraclebot.telegram.message;

import com.bolotov.oraclebot.telegram.TelegramBot;
import com.bolotov.oraclebot.telegram.message.impl.TelegramMessageMediaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramMessageFactory {

    @Autowired
    private TelegramBot bot;

    public BasicMessage newTelegramMessagePhoto(Long chatId, String fileTelegramId, String text) {
        TelegramMessageMedia messageMedia = newTelegramMessageMedia(chatId, text);
        messageMedia.addPhotoTelegramId(fileTelegramId);
        return messageMedia;
    }

    public BasicMessage newTelegramMessageVideo(Long chatId, String fileTelegramId, String text) {
        TelegramMessageMedia messageMedia = newTelegramMessageMedia(chatId, text);
        messageMedia.addVideoTelegramId(fileTelegramId);
        return messageMedia;
    }

    public TelegramMessageMedia newTelegramMessageMedia(Long chatId, String text) {
        TelegramMessageMedia messageMedia = new TelegramMessageMediaImpl();
        messageMedia.setBot(bot);
        messageMedia.setChatId(chatId);
        messageMedia.setText(text);
        return messageMedia;
    }
}
