package com.bolotov.oraclebot.service.impl;

import com.bolotov.oraclebot.model.Message;
import com.bolotov.oraclebot.model.MessageMedia;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.repository.MessageRepository;
import com.bolotov.oraclebot.service.MessageService;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMedia;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import static com.bolotov.oraclebot.model.MessageMedia.TYPE.PHOTO;
import static com.bolotov.oraclebot.model.MessageMedia.TYPE.VIDEO;

@Service
public class MessageServiceImpl implements MessageService {


    @Autowired
    private TelegramMessageFactory messageFactory;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void addPhoto(User user, String... args) {
        String photoId = args[0];
        String caption = "";
        String groupMessageId = null;
        Message message = getMessage(user.getChatId());
        if(args.length==2) {
            caption = args[1];
        }
        if(args.length==3){
            caption = args[1];
            groupMessageId = args[2];
        }
        if(groupMessageId!=null)
            message.addMediaGroup(groupMessageId, photoId, caption, PHOTO);
        else
            message.addMedia(photoId, caption, PHOTO);
    }

    @Override
    public void addVideo(User user, String... args) {
        String videoId = args[0];
        String caption = "";
        String groupMessageId = null;
        Message message = getMessage(user.getChatId());
        if(args.length==2) {
            caption = args[1];
        }
        if(args.length==3){
            caption = args[1];
            groupMessageId = args[2];
        }
        if(groupMessageId!=null)
            message.addMediaGroup(groupMessageId, videoId, caption, VIDEO);
        else
            message.addMedia(videoId, caption, VIDEO);
    }

    @Override
    public void addDocument(User user, String documentId) {
        Message message = getMessage(user.getChatId());
        message.addMedia(documentId, "", MessageMedia.TYPE.DOCUMENT);
    }

    @Override
    public void addVoice(User user, String voiceId) {
        Message message = getMessage(user.getChatId());
        message.addMedia(voiceId, "", MessageMedia.TYPE.VOICE);
    }

    @Override
    public void addText(User user, String text) {
        Message message = getMessage(user.getChatId());
        message.addText(text);
    }

    @Override
    public void removeMessage(Long userId) {
        messageRepository.removeMessage(userId);
    }


    @Override
    public void sendCurrentMessage(User user, User recipient) {
        try {
            Message message = getMessage(user.getChatId());

            for(MessageMedia msgMedia : message.getMedias()) {
                TelegramMessageMedia messageMedia = messageFactory.newTelegramMessageMedia(recipient.getChatId(), msgMedia.getCaption());
                switch (msgMedia.getType()) {
                    case VIDEO -> messageMedia.addVideoTelegramId(msgMedia.getTelegramId());
                    case PHOTO -> messageMedia.addPhotoTelegramId(msgMedia.getTelegramId());
                    /**Для документа и голосового не доделано*/
                }
                messageMedia.send();
            }

            for (Map.Entry<String, LinkedList<MessageMedia>> entry : message.getGroupMedias().entrySet()) {
                LinkedList<MessageMedia> medias = entry.getValue();
                String caption = medias.getFirst().getCaption();
                TelegramMessageMedia messageMedia = messageFactory.newTelegramMessageMedia(recipient.getChatId(), caption);
                for(MessageMedia msgMedia : medias) {
                    switch (msgMedia.getType()) {
                        case VIDEO -> messageMedia.addVideoTelegramId(msgMedia.getTelegramId());
                        case PHOTO -> messageMedia.addPhotoTelegramId(msgMedia.getTelegramId());
                    }
                }
                messageMedia.send();
            }

            for(String text : message.getTexts()) {
                TelegramMessageText messageText = messageFactory.newTelegramMessageText(recipient.getChatId(), text);
                messageText.send();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        removeMessage(user.getChatId());
    }


    private Message getMessage(Long userId) {
        Message msg = messageRepository.getMessage(userId);
        return msg;
    }

}
