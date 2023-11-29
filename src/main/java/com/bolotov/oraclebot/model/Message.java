package com.bolotov.oraclebot.model;

import com.bolotov.oraclebot.service.impl.MessageServiceImpl;

import java.util.*;

public class Message {
    private Long ownerId;

    private List<String> texts = new LinkedList<>();

    private List<MessageMedia> medias = new ArrayList<>();

    private Map<String, LinkedList<MessageMedia>> groupMedias = new HashMap<>();

    public void addText(String text) {
        texts.add(text);
    }

    public List<String> getTexts() {
        return texts;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void addMedia(String telegramId, String caption, MessageMedia.TYPE type) {
        medias.add(new MessageMedia(telegramId, caption, type));
    }

    public List<MessageMedia> getMedias() {
        return medias;
    }

    public void addMediaGroup(String groupId, String telegramId, String caption, MessageMedia.TYPE type) {
        MessageMedia media = new MessageMedia(telegramId, caption, type);
        LinkedList<MessageMedia> listMedias = groupMedias.get(groupId);
        if(listMedias == null) {
            listMedias = new LinkedList<>();
            groupMedias.put(groupId, listMedias);
        }
        listMedias.add(media);
    }

    public Map<String, LinkedList<MessageMedia>> getGroupMedias() {
        return groupMedias;
    }
}
