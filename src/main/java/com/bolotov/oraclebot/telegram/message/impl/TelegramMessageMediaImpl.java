package com.bolotov.oraclebot.telegram.message.impl;

import com.bolotov.oraclebot.telegram.message.TelegramMessage;
import com.bolotov.oraclebot.telegram.TelegramBot;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMedia;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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

    private List<InputMedia> photoMedias = new ArrayList<>();

    private List<InputMedia> videoMedias = new ArrayList<>();

    private List<String> photosId = new ArrayList<>();

    private List<String> videosId = new ArrayList<>();

    private InputFile inputFilePhoto;

    private InputFile inputFileVideo;

    @Override
    public void send() throws TelegramApiException {
        if(medias.size()>0) {
            if(medias.size()==1) {
                if(inputFilePhoto!=null) {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setCaption(text!=null ? text : "");
                    sendPhoto.setChatId(chatId);
                    sendPhoto.setPhoto(inputFilePhoto);
                    Message message = bot.execute(sendPhoto);
                    PhotoSize photoSizeMax = null;
                    for(PhotoSize photoSize : message.getPhoto()) {
                        if(photoSizeMax == null)
                            photoSizeMax = photoSize;
                        if(photoSize.getFileSize()>photoSizeMax.getFileSize())
                            photoSizeMax = photoSize;
                    }
                    if(photoSizeMax!=null)
                        photosId.add(photoSizeMax.getFileId());
                }
                if(inputFileVideo!=null){
                    SendVideo sendVideo = new SendVideo();
                    sendVideo.setCaption(text!=null ? text : "");
                    sendVideo.setChatId(chatId);
                    sendVideo.setVideo(inputFileVideo);
                    Message message = bot.execute(sendVideo);
                    photosId.add(message.getVideo().getFileId());
                }
            }
            else {
                medias.get(0).setCaption(text!=null ? text : "");
                SendMediaGroup sendMediaGroup = new SendMediaGroup(String.valueOf(chatId), medias);
                parseFileId(bot.execute(sendMediaGroup));
            }
        }
        else
            throw new TelegramApiException("Не добавлены медиа файлы");
    }

    @Override
    public TelegramMessageMedia addPhotoTelegramId(String telegramId) {
        InputMedia media = new InputMediaPhoto(telegramId);
        if(medias.size()==0) {
            inputFilePhoto = new InputFile(telegramId);
        }
        medias.add(media);
        photoMedias.add(media);
        return this;
    }

    @Override
    public TelegramMessageMedia addPhotoFile(File file) {
        InputMedia media = new InputMediaPhoto();
        media.setMedia(file, UUID.randomUUID().toString());
        if(medias.size()==0) {
            inputFilePhoto = new InputFile(file);
        }
        medias.add(media);
        photoMedias.add(media);
        return this;
    }

    @Override
    public TelegramMessageMedia addVideoTelegramId(String telegramId) {
        InputMedia media = new InputMediaPhoto(telegramId);
        if(medias.size()==0) {
            inputFileVideo = new InputFile(telegramId);
        }
        medias.add(media);
        videoMedias.add(media);
        return this;
    }

    @Override
    public TelegramMessageMedia addVideoFile(File file) {
        InputMedia media = new InputMediaVideo();
        media.setMedia(file, UUID.randomUUID().toString());
        if(medias.size()==0) {
            inputFileVideo = new InputFile(file);
        }
        medias.add(media);
        videoMedias.add(media);
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
