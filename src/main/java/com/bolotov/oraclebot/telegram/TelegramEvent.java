package com.bolotov.oraclebot.telegram;

import com.bolotov.oraclebot.telegram.message.TelegramButton;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class TelegramEvent {

    public static TelegramEvent valueOf(Update update){
        TelegramEvent event = new TelegramEvent();
        try{
            String action = "";
            String fullAction = "";
            Long chatId = 0L;
            Integer messageId = null;
            Map<String, String> values = new HashMap<>();
            if(update.hasMessage()) {
                if(update.getMessage().hasVideo()) {
                    action = "/media";
                    event.setHasVideo(true);
                    event.setVideoId(update.getMessage().getVideo().getFileId());
                    String groupId = update.getMessage().getMediaGroupId();
                    chatId = update.getMessage().getChatId();
                    event.setText(update.getMessage().getCaption());
                    if(groupId != null) {
                        event.setHasGroup(true);
                        event.setGroupId(groupId);
                    }
                }
                else if(update.getMessage().hasPhoto()) {
                    action = "/media";
                    event.setHasPhoto(true);
                    PhotoSize photoSizeMax = null;
                    for(PhotoSize photoSize : update.getMessage().getPhoto()) {
                        if(photoSizeMax == null)
                            photoSizeMax = photoSize;
                        if(photoSize.getFileSize()>photoSizeMax.getFileSize())
                            photoSizeMax = photoSize;
                    }
                    event.setPhotoId(photoSizeMax.getFileId());
                    String groupId = update.getMessage().getMediaGroupId();
                    chatId = update.getMessage().getChatId();
                    event.setText(update.getMessage().getCaption());
                    if(groupId != null) {
                        event.setHasGroup(true);
                        event.setGroupId(groupId);
                    }
                }
                else if(update.getMessage().hasText()){
                    fullAction = update.getMessage().getText();
                    action = fullAction;
                    if(!action.startsWith("/")) {
                        action = "/text";
                        event.setText(fullAction);
                    }
                    chatId = update.getMessage().getChatId();
                    event.setUsername(update.getMessage().getChat().getUserName());
                }
            } else if (update.hasCallbackQuery()) {
                String callbackText = update.getCallbackQuery().getData();
                fullAction = callbackText;
                TelegramButton button = TelegramButton.valueOf(callbackText);
                action = button.getAction();
                chatId = update.getCallbackQuery().getMessage().getChatId();
                messageId = update.getCallbackQuery().getMessage().getMessageId();
                values = button.getValues();
                event.setCallback(true);
                event.setUsername(update.getCallbackQuery().getMessage().getChat().getUserName());
            } else if (update.hasPreCheckoutQuery()) {

            }
            event.setActionName(action);
            event.setChatId(chatId);
            event.setValues(values);
            event.setMessageId(messageId);
            event.setFullAction(fullAction);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    private String actionName;

    private String fullAction;

    private Long chatId;

    private Integer messageId;

    private String groupId;

    private Map<String, String> values;

    private String text;

    private String username;

    private boolean isCallback = false;

    private boolean hasPhoto = false;

    private boolean hasVideo = false;

    private boolean hasGroup = false;

    private boolean hasDocument = false;

    private boolean hasVoice = false;

    private String photoId;

    private String videoId;

    private String documentId;

    private String voiceId;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCallback() {
        return isCallback;
    }

    public void setCallback(boolean callback) {
        isCallback = callback;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullAction() {
        return fullAction;
    }

    public void setFullAction(String fullAction) {
        this.fullAction = fullAction;
    }


    public boolean hasDocument() {
        return hasDocument;
    }


    public boolean isHasDocument() {
        return hasDocument;
    }

    public void setHasDocument(boolean hasDocument) {
        this.hasDocument = hasDocument;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

    public boolean isVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public boolean isGroup() {
        return hasGroup;
    }

    public void setHasGroup(boolean hasGroup) {
        this.hasGroup = hasGroup;
    }

    public boolean isHasVoice() {
        return hasVoice;
    }

    public void setHasVoice(boolean hasVoice) {
        this.hasVoice = hasVoice;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(String voiceId) {
        this.voiceId = voiceId;
    }
}
