package com.bolotov.oraclebot.telegram.message.impl;

import com.bolotov.oraclebot.telegram.message.TelegramMessage;
import com.bolotov.oraclebot.telegram.TelegramBot;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMedia;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.*;

public class TelegramMessageMediaImpl extends  TelegramMessageImpl implements TelegramMessageMedia {


    private List<InputMedia> medias = new ArrayList<>();

    private List<String> photosId = new ArrayList<>();

    private List<String> videosId = new ArrayList<>();

    @Override
    public void send() throws TelegramApiException {
        if(medias.size()>0) {
            medias.get(0).setCaption(text!=null ? text : "");
            SendMediaGroup sendMediaGroup = new SendMediaGroup(String.valueOf(chatId), medias);
            parseFileId(bot.execute(sendMediaGroup));
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

    public List<String> getPhotosId() {
        return photosId;
    }

    public List<String> getVideosId() {
        return videosId;
    }

    private PhotoSize getMaxPhotoSize(List<PhotoSize> photos) {
        PhotoSize maxPhotoSize = null;
        for(PhotoSize photoSize : photos) {
            int currentMaxSize = maxPhotoSize != null ? maxPhotoSize.getFileSize() : 0;
            if(photoSize.getFileSize() > currentMaxSize)
                maxPhotoSize = photoSize;
        }
        return maxPhotoSize;
    }

    private void parseFileId(List<Message> messages) {
        List<String> results = new ArrayList<>();
        for(Message message : messages) {
            if(message.hasPhoto()) {
                PhotoSize photoSize = getMaxPhotoSize(message.getPhoto());
                String telegramId = photoSize.getFileId();
                photosId.add(telegramId);
            }
            if(message.hasVideo()) {
                videosId.add(message.getVideo().getFileId());
            }
        }
    }
}
