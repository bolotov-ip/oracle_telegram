package com.bolotov.oraclebot.telegram;

import com.bolotov.oraclebot.telegram.message.TelegramButton;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
                fullAction = update.getMessage().getText();
                action = fullAction;
                if(!action.startsWith("/")) {
                    String messageText = update.getMessage().getText();
                    action = "/" + messageText.substring(0, messageText.indexOf(":"));
                    String text = messageText.substring(messageText.indexOf(" ") + 1);
                    event.setText(text);
                }
                chatId = update.getMessage().getChatId();
                event.setUsername(update.getMessage().getChat().getUserName());
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

    private Map<String, String> values;

    private String text;

    private String username;

    private boolean isCallback = false;

    private boolean hasMedia = false;

    private Set<String> photos = new HashSet<>();

    private Set<String> videos = new HashSet<>();

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

    public boolean isHasMedia() {
        return hasMedia;
    }

    public void setHasMedia(boolean hasMedia) {
        this.hasMedia = hasMedia;
    }

    public Set<String> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<String> photos) {
        this.photos = photos;
    }

    public Set<String> getVideos() {
        return videos;
    }

    public void setVideos(Set<String> videos) {
        this.videos = videos;
    }
}
