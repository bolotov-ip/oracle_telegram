package com.bolotov.oraclebot.telegram.message.impl;

import com.bolotov.oraclebot.telegram.TelegramBot;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.message.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramMessageFactoryImpl implements TelegramMessageFactory {

    @Autowired
    private TelegramBot bot;

    public TelegramMessage newTelegramMessagePhoto(Long chatId, String fileTelegramId, String text) {
        TelegramMessageMedia messageMedia = newTelegramMessageMedia(chatId, text);
        messageMedia.addPhotoTelegramId(fileTelegramId);
        return messageMedia;
    }

    public TelegramMessage newTelegramMessageVideo(Long chatId, String fileTelegramId, String text) {
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

    public TelegramMessageText newTelegramAdaptiveMessageText(TelegramEvent event, String text) {
        TelegramMessageText messageText = null;
        if(event.isCallback()) {
            messageText = new TelegramMessageEditTextImpl();
            ((TelegramMessageEditText)messageText).setMessageId(event.getMessageId());
        }
        else
            messageText= new TelegramMessageTextImpl();
        messageText.setBot(bot);
        messageText.setAction(event.getActionName());
        messageText.setChatId(event.getChatId());
        messageText.setText(text);
        messageText.setFullAction(event.getFullAction());
        String numberPage = event.getValues().get("np");
        messageText.setCurrentPage(Integer.valueOf(numberPage==null? "1"  : numberPage));
        return messageText;
    }

    @Override
    public TelegramMessageText newTelegramMessageText(TelegramEvent event, String text) {
        TelegramMessageText messageText = new TelegramMessageTextImpl();
        messageText.setBot(bot);
        messageText.setAction(event.getActionName());
        messageText.setChatId(event.getChatId());
        messageText.setText(text);
        messageText.setFullAction(event.getFullAction());
        String numberPage = event.getValues().get("np");
        messageText.setCurrentPage(Integer.valueOf(numberPage==null? "1"  : numberPage));
        return messageText;
    }

    @Override
    public TelegramMessageMenu newTelegramMessageMenu(Long chatId) {
        TelegramMessageMenu messageMenu = new TelegramMessageMenuImpl();
        messageMenu.setBot(bot);
        messageMenu.setChatId(chatId);
        return messageMenu;
    }
}
