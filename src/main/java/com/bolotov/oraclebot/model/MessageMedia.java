package com.bolotov.oraclebot.model;

import com.bolotov.oraclebot.service.impl.MessageServiceImpl;

public class MessageMedia {
    public MessageMedia(String telegramId, String caption, MessageMedia.TYPE type) {
        this.telegramId = telegramId;
        this.caption = caption;
        this.type = type;
    }

    public enum TYPE {PHOTO, VIDEO, DOCUMENT, VOICE}

    private String telegramId;

    private String caption;

    private MessageMedia.TYPE type;

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public MessageMedia.TYPE getType() {
        return type;
    }

    public void setType(MessageMedia.TYPE type) {
        this.type = type;
    }
}
