package com.bolotov.oraclebot.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.*;

public class TelegramMessageMediaImpl implements TelegramMessageMedia{

    private TelegramBot bot;

    private String text;

    private Long chatId;

    private List<InputMedia> medias = new ArrayList<>();

    public Long getChatId() {
        return chatId;
    }

    @Override
    public TelegramMessageMedia setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    @Override
    public BasicMessage setBot(TelegramBot bot) {
        this.bot = bot;
        return this;
    }

    @Override
    public BasicMessage setText(String text) {
        this.text = text;
        return null;
    }

    @Override
    public List<Message> send() throws TelegramApiException {
        if(medias.size()>0) {
            medias.get(0).setCaption(text!=null ? text : "");
            SendMediaGroup sendMediaGroup = new SendMediaGroup(String.valueOf(chatId), medias);
            return bot.execute(sendMediaGroup);
        }
        else
            throw new TelegramApiException("Не добавлены медиа файлы");
    }

    @Override
    public TelegramMessageMedia addPhotoTelegramId(String telegramId) {
        medias.add(new InputMediaPhoto(telegramId));
        return this;
    }

    @Override
    public TelegramMessageMedia addPhotoFile(File file) {
        InputMedia media = new InputMediaPhoto();
        media.setMedia(file, UUID.randomUUID().toString());
        medias.add(media);
        return this;
    }

    @Override
    public TelegramMessageMedia addVideoTelegramId(String telegramId) {
        medias.add(new InputMediaVideo(telegramId));
        return this;
    }

    @Override
    public TelegramMessageMedia addVideoFile(File file) {
        InputMedia media = new InputMediaVideo();
        media.setMedia(file, UUID.randomUUID().toString());
        medias.add(media);
        return this;
    }
}
